import { StyleSheet, View } from 'react-native';
import { LiteImage } from 'react-native-lite-image';
import { getAllCache, preload } from 'react-native-lite-image-control';
getAllCache().then((cacheList) => {
  console.log('Cache files:', cacheList);
});
preload([
  'https://picsum.photos/404',
  'https://picsum.photos/405',
  'https://picsum.photos/406',
]);
export default function App() {
  return (
    <View style={styles.container}>
      <LiteImage
        source={{ uri: 'https://picsum.photos/404' }}
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
