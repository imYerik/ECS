import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class ECS_S3 {
	public static String uid = "user01";	              //object user of ECS, not management user;
	public static String secret = "3Ga/y7Fiixit9auihdc57aiLLchnr8g07nLJkSl6";  //generate when create object user; 
	public static String viprDataNode = "http://172.16.0.1:9020";    //ECS node ENDPOINT
	
	public static void main(String[] args) throws Exception {
		AmazonS3Client s3client = new AmazonS3Client(new BasicAWSCredentials(uid, secret));		
		s3client.setEndpoint(viprDataNode);
		
//		创建Bucket
		createBucket(s3client,"testbucket");
		
//		上传文件至Bucket
		File objectFile = new File("/Users/Yerik/Downloads/IMG_5022_Snapseed.jpg");
		putObjectFromFile( s3client, "testbucket", objectFile);
		putObjectFromInputStream(s3client, "testbucket", objectFile);
		
		
//		列出Bucket所有Object
		 listObjects(s3client,"testbucket");

//		 复制Object
		 copyObject(s3client,"testbucket","IMG_5022_Snapseed.jpg","bucket01","new_IMG_5022_Snapseed.jpg");
		 listObjects(s3client,"bucket01");

		 
//		下载Object到本地
		 getObject( s3client, "testbucket", "IMG_5022_Snapseed.jpg","/Users/Yerik/Downloads/","download_IMG_5022_Snapseed.jpg");
		 
//		 生成ObjectURL
		 generateObjectUrl(s3client,"testbucket","IMG_5022_Snapseed.jpg");
		 
//		 删除Object
		 deleteObject(s3client,"bucket01","new_IMG_5022_Snapseed.jpg");
		 deleteObject(s3client,"testbucket","IMG_5022_Snapseed.jpg");
		 listObjects(s3client,"bucket01");
		 
//			删除Bucket
			deleteBucket(s3client,"testbucket");
	}
	

	
	public static void createBucket(AmazonS3Client client, String bucketName){
		Bucket bucket =  client.createBucket(bucketName);
		if(bucket != null){
			System.out.println("创建Bucket成功："+ bucket.toString());
		}	
	}
	
	public static void deleteBucket(AmazonS3Client client, String bucketName){
		client.deleteBucket(bucketName);
	}
	
	public static void putObjectFromFile(AmazonS3Client client, String bucketName,File objectFile){
		client.putObject(bucketName, objectFile.getName(), objectFile);
	}
	
	public static void putObjectFromInputStream(AmazonS3Client client, String bucketName,File objectFile) throws FileNotFoundException{
		InputStream input = new FileInputStream(objectFile);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.addUserMetadata("location", "Tiebet");
		client.putObject(bucketName, objectFile.getName(), input, metadata);
	}
	
	public static void getObject(AmazonS3Client client, String bucketName,String objectKey, String path, String filename) throws IOException{
		S3Object object = client.getObject(bucketName, objectKey);
		InputStream reader = new BufferedInputStream(object.getObjectContent());
		File file = new File(path+filename);      
		OutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
		int read = -1;
		while ( ( read = reader.read() ) != -1 ) {
			 writer.write(read);
		}
		writer.flush();
		writer.close();
		reader.close();
	}
	public static void generateObjectUrl(AmazonS3Client client, String bucketName,String objectKey){
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectKey);  
	    System.out.println("Object("+objectKey+")链接："+client.generatePresignedUrl(request));  
//	    http://172.16.0.221:9020/testbucket/IMG_5022_Snapseed.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20161213T041656Z&X-Amz-SignedHeaders=host&X-Amz-Expires=900&X-Amz-Credential=user01%2F20161213%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=5ebcf35fde6171cf9491149a9a3eedf1e6b3d73145c7dbc404756554b5b4c78f

	}
	
	public static void copyObject(AmazonS3Client client, String sourceBucketName,String sourceKey,String destinationBucketName,String destinationKey){
		client.copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey);
	}
	
	public static void deleteObject(AmazonS3Client client, String bucketName, String objectKey){
		client.deleteObject(bucketName, objectKey);
		
	}
	
	public static void listObjects(AmazonS3Client client, String bucketName) {
		ObjectListing objects = client.listObjects(bucketName);
		for (S3ObjectSummary summary : objects.getObjectSummaries()) {
			System.out.println("列出Bucket("+bucketName+")下所有Object："+summary.getKey()+ " "+summary.getOwner());
		}
	}
	
	
}
