scp -r /home/leonardo/Documentos/sockets leonardo@192.168.1.102:
ssh leonardo@192.168.1.102 '(cd ~/sockets/src/logic; sh compilar.sh; java Runner s; exit)' &

java Runner c 192.168.1.102
