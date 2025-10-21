import { codegenNativeComponent, type ViewProps } from 'react-native';
import type { Double } from 'react-native/Libraries/Types/CodegenTypes';

export interface NativeProps extends ViewProps {
  source?: {
    uri: string;
  };
  resizeMode?: string;
  cacheTTL?: Double;
}

export default codegenNativeComponent<NativeProps>('LiteImageViewView');
