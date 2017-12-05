<?php
require"init.php";
$Email=$_POST["Email"];
$password=$_POST["Password"];
//$Email="ghvjh";
//$password="65765";

$sql_query="INSERT INTO registration ( Email,password) 
				VALUES ('$Email','$password')";
if(mysqli_query($con,$sql_query))
echo "<h1>registration Success</h1>";
else
echo "error";
?>