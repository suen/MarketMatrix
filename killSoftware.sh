for i in `ps | grep java | cut -f2 -d" "`; do kill -9 $i; done
