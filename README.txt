cd to /src/main/ui
run 'npm install' to install node_modules

when running on AWS ubuntu ec2 server ... 
-setup mysql
-sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 8080
-sudo iptables -t nat -I OUTPUT -p tcp -d 127.0.0.1 --dport 80 -j REDIRECT --to-ports 8080
-install chrome driver "sudo wget https://chromedriver.storage.googleapis.com/80.0.3987.106/chromedriver_linux64.zip"
-"sudo unzip chromedriver_linux64.zip"
-install chrome binary "sudo apt-get install -f" "wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb" "sudo dpkg -i google-chrome-stable_current_amd64.deb
"
