//
//  LiteImageViewUI.swift
//  LiteImageView
//
//  Created by Thong Luong on 21/10/25.
//

import Foundation
import UIKit
import SDWebImage

@objc public class LiteImageViewUI: UIView {

  private let imageView = UIImageView()

  /// TTL in seconds. Default 1 hour.
  @objc public var cacheTTL: TimeInterval = 3600

  private let userDefaultsKeyPrefix = "LiteImageViewCacheTS_"

  @objc public override init(frame: CGRect) {
    super.init(frame: frame)
    setupView()
  }

  required init?(coder: NSCoder) {
    super.init(coder: coder)
    setupView()
  }

  private func setupView() {
    addSubview(imageView)
    imageView.clipsToBounds = true
    imageView.translatesAutoresizingMaskIntoConstraints = false

    NSLayoutConstraint.activate([
      imageView.leadingAnchor.constraint(equalTo: leadingAnchor),
      imageView.trailingAnchor.constraint(equalTo: trailingAnchor),
      imageView.topAnchor.constraint(equalTo: topAnchor),
      imageView.bottomAnchor.constraint(equalTo: bottomAnchor)
    ])

    imageView.contentMode = .scaleAspectFill
  }

  @objc public func loadImage(_ uri: NSString?, resizeMode: NSString?) {
    guard let uri = uri as String?, let url = URL(string: uri) else {
      imageView.image = nil
      return
    }

    // ✅ Apply resize mode
    if let mode = resizeMode as String? {
      switch mode {
      case "contain": imageView.contentMode = .scaleAspectFit
      case "stretch": imageView.contentMode = .scaleToFill
      case "center": imageView.contentMode = .center
      default: imageView.contentMode = .scaleAspectFill
      }
    }

    // ✅ Build cache key
    let cacheKey = SDWebImageManager.shared.cacheKey(for: url)

    if let key = cacheKey {
      if let cachedImage = SDImageCache.shared.imageFromCache(forKey: key) {
        let tsKey = userDefaultsKeyPrefix + key
        let timestamp = UserDefaults.standard.double(forKey: tsKey)
        if timestamp > 0 {
          let now = Date().timeIntervalSince1970
          if now - timestamp < cacheTTL {
            DispatchQueue.main.async { [weak self] in
              self?.imageView.image = cachedImage
            }
            return
          } else {
            // ❌ Expired -> remove
            SDImageCache.shared.removeImage(forKey: key, fromDisk: true) {
              UserDefaults.standard.removeObject(forKey: tsKey)
            }
          }
        } else {
          SDImageCache.shared.removeImage(forKey: key, fromDisk: true) {
            UserDefaults.standard.removeObject(forKey: self.userDefaultsKeyPrefix + key)
          }
        }
      }
    }

    // ✅ Download and store with TTL timestamp
    let options: SDWebImageOptions = [.retryFailed, .scaleDownLargeImages, .continueInBackground]
    imageView.sd_setImage(with: url, placeholderImage: nil, options: options) { [weak self] image, _, _, imageURL in
      guard let self = self, let image = image, let imageURL = imageURL else { return }

      if let key = SDWebImageManager.shared.cacheKey(for: imageURL) {
        let tsKey = self.userDefaultsKeyPrefix + key
        UserDefaults.standard.set(Date().timeIntervalSince1970, forKey: tsKey)
        SDImageCache.shared.store(image, forKey: key, toDisk: true, completion: nil)
      }
    }
  }

  /// Optional helper to clear all LiteImageView TTL metadata.
  @objc public func clearTimestamps() {
    let ud = UserDefaults.standard
    for (key, _) in ud.dictionaryRepresentation() where key.hasPrefix(userDefaultsKeyPrefix) {
      ud.removeObject(forKey: key)
    }
    ud.synchronize()
  }
}
