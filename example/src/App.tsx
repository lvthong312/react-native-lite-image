import { StyleSheet, View } from 'react-native';
import { LiteImage } from 'react-native-lite-image';
import { getAllCache, preload } from 'react-native-lite-image-control';
getAllCache().then((cacheList) => {
  console.log('Cache files:', cacheList);
});
preload([
  'https://.../404',
]);
export default function App() {
  return (
    <View style={styles.container}>
      <LiteImage
        source={require('./download-1.jpg')}
        style={styles.box}
        cacheTTL={600}
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
