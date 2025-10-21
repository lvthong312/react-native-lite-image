#import "LiteImageViewView.h"

#import <react/renderer/components/LiteImageViewViewSpec/ComponentDescriptors.h>
#import <react/renderer/components/LiteImageViewViewSpec/EventEmitters.h>
#import <react/renderer/components/LiteImageViewViewSpec/Props.h>
#import <react/renderer/components/LiteImageViewViewSpec/RCTComponentViewHelpers.h>

#import "RCTFabricComponentsPlugins.h"
#import "LiteImageView-Swift.h"

using namespace facebook::react;

@interface LiteImageViewView () <RCTLiteImageViewViewViewProtocol>
@end

@implementation LiteImageViewView {
  LiteImageViewUI *_imageView; // ✅ Swift UIView thực thi việc hiển thị
}

+ (ComponentDescriptorProvider)componentDescriptorProvider {
  return concreteComponentDescriptorProvider<LiteImageViewViewComponentDescriptor>();
}

- (instancetype)initWithFrame:(CGRect)frame {
  if (self = [super initWithFrame:frame]) {
    static const auto defaultProps = std::make_shared<const LiteImageViewViewProps>();
    _props = defaultProps;

    // ✅ Gắn UIView Swift làm contentView
    _imageView = [LiteImageViewUI new];
    self.contentView = _imageView;
  }
  return self;
}

- (void)updateProps:(Props::Shared const &)props oldProps:(Props::Shared const &)oldProps {
  const auto &oldViewProps = *std::static_pointer_cast<LiteImageViewViewProps const>(_props);
  const auto &newViewProps = *std::static_pointer_cast<LiteImageViewViewProps const>(props);
  // ✅ Update cacheTTL
  if (oldViewProps.cacheTTL != newViewProps.cacheTTL) {
    _imageView.cacheTTL = newViewProps.cacheTTL;
  }
  // ✅ Cập nhật khi source hoặc resizeMode thay đổi
  if (oldViewProps.source.uri != newViewProps.source.uri ||
      oldViewProps.resizeMode != newViewProps.resizeMode) {

    NSString *uriString = [[NSString alloc] initWithUTF8String:newViewProps.source.uri.c_str()];
    NSString *resizeMode = nil;

    if (!newViewProps.resizeMode.empty()) {
      resizeMode = [NSString stringWithUTF8String:newViewProps.resizeMode.c_str()];
    }

    [_imageView loadImage:uriString resizeMode:resizeMode];
  }

  [super updateProps:props oldProps:oldProps];
}

Class<RCTComponentViewProtocol> LiteImageViewViewCls(void) {
  return LiteImageViewView.class;
}

@end
