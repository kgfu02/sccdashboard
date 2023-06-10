![alt text](https://github.com/kgfu02/sccdashboard/assets/51866248/45668481-93a3-4acd-9f10-1ff1370cfc1a)
cd to /src/main/ui<br />
run 'npm install' to install node_modules<br />
'mvn package' to create .jar file<br />

when running on AWS ubuntu ec2 server ... <br />
-setup mysql<br />
-sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 8080<br />
-sudo iptables -t nat -I OUTPUT -p tcp -d 127.0.0.1 --dport 80 -j REDIRECT --to-ports 8080<br />
-install chrome driver "sudo wget https://chromedriver.storage.googleapis.com/80.0.3987.106/chromedriver_linux64.zip"<br />
-"sudo unzip chromedriver_linux64.zip"<br />
-install chrome binary "sudo apt-get install -f" "wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb" "sudo dpkg -i google-chrome-stable_current_amd64.deb
"
