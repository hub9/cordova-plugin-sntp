# cordova-plugin-sntp

Simple plugin to connect into SNTP servers and get accurate time.

Currently it is implemented for Android and has a dummy implementation for iOS. Since iOS keeps the device synced with NTP servers, it's not a common problem to have system clock unsynchronized, but Android only seems to do this on boot.

### Installation

```bash
$ cordova plugin add https://github.com/hub9co/cordova-plugin-sntp
```

### Usage

Initialization
```javascript
document.addEventListener("deviceready", onDeviceReady, false);
function onDeviceReady() {
    // Set the SNTP server and timeout
    cordova.plugins.sntp.setServer("a.st1.ntp.br", 10000);
}
```

Get time since epoch in milliseconds:
```javascript
cordova.plugins.sntp.getTime(
    function(time) {
        console.log("The actual amount of milliseconds since epoch is:", time);
    },
    function(errorMessage) {
        console.log("I haz error:", errorMessage);
    }
);
```

Get system clock offset:
```javascript
cordova.plugins.sntp.getClockOffset(
    function(offset) {
        console.log("System clock offset is:", offset);
    },
    function(errorMessage) {
        console.log("I haz error:", errorMessage);
    }
);
```
