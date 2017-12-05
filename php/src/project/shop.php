<?php
require"init.php";
//$shop_name="hhh";
//$category="d";
//$email="Mehadi";

$shop_name=$_POST["Email"];
$category=$_POST["Password"];
$email=$_POST["email"];

$sql_qury="SELECT * FROM registration WHERE Email like '$email'";
$result=mysqli_query($con,$sql_qury);
if($row=mysqli_fetch_assoc($result))
	$id=$row['res_id'];

$sql_query="INSERT INTO shop (shop_name,category,res_id) VALUES ('$shop_name','$category','$id')";

if(mysqli_query($con,$sql_query))
{
echo "Data insertion success...";
}
else
{
echo "Data insertion error....",mysqli_error($con);

}
?>