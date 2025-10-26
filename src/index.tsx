import { Image } from 'react-native';
import type { NativeProps } from './LiteImageViewViewNativeComponent';

import LiteImageView from './LiteImageViewViewNativeComponent';
export * from './LiteImageViewViewNativeComponent';

export const LiteImage = ({ source, ...rest }: NativeProps) => {
  let uri = null;

  if (typeof source === 'number') {
    const resolved = Image.resolveAssetSource(source);
    uri = resolved?.uri ?? null;
  } else if (typeof source === 'object' && source?.uri) {
    uri = source.uri;
  } else if (typeof source === 'string') {
    uri = source;
  }

  return <LiteImageView source={{ uri: uri || '' }} {...rest} />;
};
