#ServerChat  
##keygen  
(java keytool)  
keytool -genkey -keyalg RSA -keystore "server.cer"  
And get ready for the following steps  
##Usage  
1. javac all files into /bin  
2. cd bin  
3. mv path/to/server.cer .  
\(Start Client\): java me/petjelinux/ServerChat/Client/CMD \[server.cer's password] \[server's ip address] \[server's port]  
\(Start Server\): java me/petjelinux/ServerChat/Server/MultiThreadServer \[listen port] \[server.cer's password]  

##What's this?  
This is my SSL API usage practice.  
This tiny program can use TLS to communicate with others.  
##Commands  
SAY [message](send message)
JOIN [channel](JOIN a channel | default: you are at "lobby")  
VIEW [channel](VIEW a channel's log)  
NICK [nickname](change your nickname | default: unset)  
