import { StyleSheet, View } from 'react-native';
import { LiteImage } from 'react-native-lite-image';

export default function App() {
  return (
    <View style={styles.container}>
      <LiteImage
        source={{ uri: 'https://picsum.photos/315' }}
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
