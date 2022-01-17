#!/bin/bash

# 脚本参数说明
# $1 backupDir
# $2 mysqlDir
# $3 BinFile
# $4 mysql host
# $5 mysql port
# $6 mysql username
# $7 mysql password


#在使用之前，请提前创建以下各个目录
#增量备份时复制mysql-bin.00000*的目标目录，提前手动创建这个目录
backupDir=${1-/usr/local/work/backup/daily}
#mysql的数据目录
mysqlDir=${2-/var/lib/mysql}
#mysql的index文件路径，放在数据目录下的
BinFile=${3-/var/lib/mysql/mysql-bin.index}

MY_HOST=${4-localhost}
MY_PORT=${5-3306}
MY_USERNAME=${6-root}
MY_PASSWORD=${7-root}

#这个是用于产生新的mysql-bin.00000*文件
mysqladmin --host=${MY_HOST} --port=${MY_PORT} -u${MY_USERNAME} --password="${MY_PASSWORD}" flush-logs

# wc -l 统计行数
# awk 简单来说awk就是把文件逐行的读入，以空格为默认分隔符将每行切片，切开的部分再进行各种分析处理。
Counter=`wc -l $BinFile |awk '{print $1}'`
NextNum=0
#这个for循环用于比对$Counter,$NextNum这两个值来确定文件是不是存在或最新的
for file in `cat $BinFile`
do
    base=`basename $file`
    echo $base
    #basename用于截取mysql-bin.00000*文件名，去掉./mysql-bin.000005前面的./
    NextNum=`expr $NextNum + 1`
    if [ $NextNum -eq $Counter ]
    then
        echo $base skip!
    else
        dest=$backupDir/$base
        if(test -e $dest)
        #test -e用于检测目标文件是否存在，存在就写exist!到$logFile去
        then
            echo $base exist!
        else
            cp $mysqlDir/$base $backupDir
            echo $base copying
         fi
     fi
done
echo `date +"%Y年%m月%d日 %H:%M:%S"` $Next Bakup succ!

# 使用 nodejs 上传备份文件到 私有云
#NODE_ENV=$backUpFolder@$backUpFileName /root/node/v8.11.3/bin/node /usr/local/upload.js

# 使用 python 上传备份文件到 私有云
#python /use/local/upload.py $backUpFolder $backUpFileName