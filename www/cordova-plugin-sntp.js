var exec = require('cordova/exec');

function sntp() {};

sntp.setServer = function(sntpServer, timeout) {
    exec(null, null, "cordova-plugin-sntp", "setServer", [sntpServer, timeout]);
}

sntp.getTime = function(success, error) {
    exec(success, error, "cordova-plugin-sntp", "getTime", []);
};

sntp.getClockOffset = function(success, error) {
    exec(success, error, "cordova-plugin-sntp", "getClockOffset", []);
};

module.exports = sntp;
