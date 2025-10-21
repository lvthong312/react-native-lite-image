# react-native-lite-image-view

Support for View Image

<p align="center">
  <img src="https://img.shields.io/npm/v/react-native-lite-image?color=green" alt="npm version" />
  <img src="https://img.shields.io/npm/dm/react-native-lite-image" alt="npm downloads" />
  <img src="https://img.shields.io/badge/react--native-0.70+-blue" alt="react-native" />
</p>


## Installation


```sh
npm install react-native-lite-image-view
```

## For IOS
Go to ios/Podfile add `pod 'SDWebImage', :modular_headers => true` like bellow

```sh

...
  target 'LiteImageViewExample' do
  config = use_native_modules!
  pod 'SDWebImage', :modular_headers => true # Add this line
  use_react_native!(
    :path => config[:reactNativePath],
    # An absolute path to your application root.
    :app_path => "#{Pod::Config.instance.installation_root}/.."
  )
....

```
## For Android
Don't need do anything

## Usage

```js
import { StyleSheet, View } from 'react-native';
import { LiteImage } from 'react-native-lite-image';

export default function App() {
  return (
    <View style={styles.container}>
      <LiteImage
        source={{ uri: '...' }}
        style={styles.box}
        cacheTTL={5}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 300,
    height: 300,
    resizeMode: 'contain',
  },
});

```

# ⚙️ Props
| Prop         | Type                                            | Default      | Description                                          |
| ------------ | ----------------------------------------------- | ------------ | ---------------------------------------------------- |
| `source`     | `{ uri: string }` or `number`                   | **required** | The image source (remote URL or local file)          |
| `style`      | `StyleProp<ImageStyle>`                         | -            | The image style (width, height, etc.)                |
| `cacheTTL`   | `number`                                        | `0`          | Cache duration in minutes (0 = no cache)             |
| `resizeMode` | `'cover' \| 'contain' \| 'stretch' \| 'center'` | `'cover'`    | How the image should be resized to fit the container |
