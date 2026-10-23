#!/system/bin/sh
input tap 942 648
sleep 4
(sleep 0.2; input tap 150 1620) &
i=1
while [ $i -le 12 ]; do
  screencap /sdcard/rw$i.rgba
  i=$((i+1))
done
