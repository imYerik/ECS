# ECS S3 Sample Code 

```Java
public static String uid = "user01";                                        //Object User of ECS Namespace
public static String secret = "3Ga/yjEiimit9Cyihdc97fkLLMhmrhg07nLJxSl6";  //S3 Key
public static String viprDataNode = "http://172.16.0.1:9020";              //ECS IP or Load balance VIP
```

### 创建Bucket
```Java
		createBucket(s3client,"testbucket");
```

### 上传文件至Bucket
```Java
		putObjectFromFile( s3client, "testbucket", objectFile);
```
### 通过文件流上传至Bucket
```Java
		putObjectFromInputStream(s3client, "testbucket", objectFile);
```		
		
### 列出Bucket所有Object 
```Java
		 listObjects(s3client,"testbucket");
```
### 复制Object  
```Java
		 copyObject(s3client,"testbucket","IMG_5022_Snapseed.jpg","bucket01","new_IMG_5022_Snapseed.jpg");
```
		 
### 下载Object到本地  
```Java
		 getObject( s3client, "testbucket", "IMG_5022_Snapseed.jpg","/Users/Yerik/Downloads/","download_IMG_5022_Snapseed.jpg");
```

### 生成ObjectURL  
```Java
		 generateObjectUrl(s3client,"testbucket","IMG_5022_Snapseed.jpg");
```
		 
### 删除Object  
```Java
		 deleteObject(s3client,"bucket01","new_IMG_5022_Snapseed.jpg");
```
		 
### 删除Bucket  
```Java
		 deleteBucket(s3client,"testbucket");
```
