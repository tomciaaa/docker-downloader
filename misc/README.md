# Running as a service

It's recommended to run this behind something like nginx. A very dumb nginx setup, requires just an nginx install, and setting proxy pass:
```
```

Also if you run into `(13 permission denied)` errors, it's probably SELinux rearing its ugly head and you can tell it to buzz off like so [SO post](https://stackoverflow.com/questions/23948527/13-permission-denied-while-connecting-to-upstreamnginx):

```
setsebool -P httpd_can_network_connect 1
```

## Install packaged service
After copying the jar to /opt/gs-spring-boot-0.1.0.jar, you can stick the docker-downloader.service with the others. Typically, /usr/lib/systemd/system:
```
sudo cp  docker-downloader.service /usr/lib/systemd/system/docker-downloader.service
```

## Enable at boot
```
sudo systemctl enable docker-downloader
```
