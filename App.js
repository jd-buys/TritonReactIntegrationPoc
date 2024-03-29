/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, {useState} from 'react';
import { DeviceEventEmitter, NativeModules, NativeEventEmitter, Platform } from 'react-native';
import type {Node} from 'react';
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
  Button,
  Alert
} from 'react-native';

import {
  Colors,
  DebugInstructions,
  Header,
  LearnMoreLinks,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

const { TritonSDKModule } = NativeModules;

const onPlayPress = () => {
  console.log('Playing....');
  TritonSDKModule.play('TRITONRADIOMUSICAAC');
};

const onStopPress = () => {
  console.log('Stopping....');
  TritonSDKModule.stop();
};

const App: () => Node = () => {
  
  const [statusText, setStatusText] = useState("Stopped");

  if (Platform.OS === 'ios') {
    const tritonEmitter = new NativeEventEmitter(NativeRNTritonPlayer);
    tritonEmitter.addListener('stateChanged', (playerState)=> {console.log("The State:" + playerState)});
  } else {
    DeviceEventEmitter.addListener('stateChanged', playerState => {
      console.log("The State:" + JSON.stringify(playerState));
      setStatusText(playerState.state)
    });
  }  

  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}>
       
        <View
          style={{
            backgroundColor: isDarkMode ? Colors.black : Colors.white,
          }}>
         <Button
          onPress={onPlayPress}
          title="Play"
          color="#186494"
          accessibilityLabel="Play Station"
      />
      <Button
          onPress={onStopPress}
          title="Stop"
          color="#186494"
          accessibilityLabel="Stop Station"
      />
        </View>
       <Text>The Player Status: {statusText}</Text>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});

export default App;
