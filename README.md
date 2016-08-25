# Security
### A Plugin that makes managing player access to areas easy!

Codacy Code Quality Rating: [![Codacy Badge](https://api.codacy.com/project/badge/Grade/e37cff79f84c4d76bbf6971dc94db160)]

*This plugin will prevent the player from jumping over specific blocks and, with various config options, can be configured to whatever you may need! The default blocks that players are not allowed over are Fences and Iron bars. This was mainly designed for a theme park server for the queues so I do not recommend using this on a survival server as this would impede on the users quite a lot.*

**IMPORTANT PLEASE READ!**
When updating the plugin you have to delete the old config to allow newer config options to be available! This will be fixed in a newer update!

**_It is not recommended to use this on a large server as it could create unnecessary lag because it calls a function every time a player moves! This cannot be fixed at the moment so please do not ask for it although I will try and find a solution for this._**

###Config File
```yml
#List of blocks to not allow normal people over
blacklist:
- IRON_FENCE
- FENCE
- BARRIER

#The prefix that will go before every message
prefix: "&cSecurity>"

#The message the user is sent when trying to jump over a restricted block
disallow-message: "&rYou cannot do this!"

#Whether to notify people with the "security.notify" permission that a player is jumping over a restricted block
notify-admins: false
#The message that the admin receives ($PLAYER is replaced with the player's name)
notify-message: "&c$PLAYER is trying to jump the fence!"

#Whether to log players' actions to a file (in the plugin/Security/logs folder)
log: true
#The message that is printed in the file to notify anyone with access that the player is trying to jump
#over a restricted block ($PLAYER is the player's name while $COORDS is the player's co-ordinates)
log-message: "$PLAYER tried to jump the fence at $COORDS!"

#Whether to warn the player of their wrong doings
warnings-enabled: true
#How many warnings the player should receive before the command is sent
warnings-max: 5
#The command to execute when the max number of warnings has been reached
warnings-command: "/spawn $PLAYER"
#The message that accompanies the command
warnings-message: "You have been escorted out by security!"
#Whether to tell the player after every warning has passed
warnings-warn-msg-enabled: true
#The message to show upon every warning being used ($WARNINGS can be used for the warnings left)
warnings-warn-message: "Don't do that! You have &c$WARNINGS&r warnings left!"

#How often (In seconds) should a message be sent? (WARNING: Setting this to 0 will cause major spamming!)
message-delay: 2

#Whether OPs should be able to bypass the permissions by default
op-bypass: true
```

###Permissions
```
security.bypass - Allows the player to bypass the protection
security.notify - Notifies the player if another player is attempting to jump over restricted blocks
security.* - Gives the user full admin access to the plugin
```

###Change Log
```
V1.5
- Minor plugin improvements
V1
- First Ever version of this plugin!
- Permissions
- Logging
- Warning
- Notifying admins
- Etc Etc
```
