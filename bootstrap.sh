# Install MongoDB
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10
echo "deb http://repo.mongodb.org/apt/ubuntu trusty/mongodb-org/3.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.0.list

#Update
sudo apt-get update

# Install Open-JDK Java8
sudo apt-get -y install openjdk-8-jre openjdk-8-jdk

#Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

# Install Mongo and Java
sudo apt-get install -y mongodb-org

sudo systemctl mongod start

##Install Maven3
wget http://www.gtlib.gatech.edu/pub/apache/maven/maven-3/3.0.5/binaries/apache-maven-3.0.5-bin.tar.gz
tar -zxf apache-maven-3.0.5-bin.tar.gz
cp -R apache-maven-3.0.5 /usr/local/
ln -s /usr/local/apache-maven-3.0.5/bin/mvn  /usr/bin/mvn

# Install pip
sudo apt-get install python-pip python-dev build-essential 
sudo pip install --upgrade pip 
