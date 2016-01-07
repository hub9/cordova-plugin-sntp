#import <Cordova/CDV.h>

@interface CDVPluginSntp : CDVPlugin

- (void)setServer:(CDVInvokedUrlCommand*)command;
- (void)getTime:(CDVInvokedUrlCommand*)command
- (void)getTimeOffset:(CDVInvokedUrlCommand*)command;

@end
