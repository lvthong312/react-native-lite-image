import { Image } from 'react-native';
import type { NativeProps } from './LiteImageViewViewNativeComponent';
import LiteImageView from './LiteImageViewViewNativeComponent';
export * from './LiteImageViewViewNativeComponent';

export const LiteImage = ({ source, ...rest }: NativeProps) => {
  if (typeof source === 'number') {
    const resolved = Image.resolveAssetSource(source);
    return (
      <LiteImageView
        source={{
          uri: resolved?.uri || '',
        }}
        {...rest}
      />
    );
  } else if (typeof source === 'object' && source?.uri) {
    return <LiteImageView source={source} {...rest} />;
  }

  return <LiteImageView {...rest} />;
};
