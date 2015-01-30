for i in `ps | grep java | cut -f2 -d" "`; do kill -9 $i; done 2> /dev/null
if [ $? -eq 1 ]; then
	for i in `ps | grep java | cut -f1 -d" "`; do kill -9 $i; done
fi
