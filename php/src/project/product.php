<?php
require"init.php";

$product_name=$_POST["pnamed"];
$quantity=$_POST["quantity"];
$price=$_POST["price"];
$email=$_POST["email"];
$type=$_POST["type"];
$pname=$_POST["prev_name"];

// $product_name="mal";
// $quantity="sal";
// $price="1";
// $email="Mehadi";
// $type="A";
//  $pname="m";

$sql="Select * from registration where Email like '$email'";
$result=mysqli_query($con,$sql);
$row=mysqli_fetch_assoc($result);
$person_id=$row['res_id'];

$sql_qury2="SELECT * FROM shop WHERE res_id like '$person_id'";
$result2=mysqli_query($con,$sql_qury2);
if($row2=mysqli_fetch_assoc($result2))
	$id=$row2['shop_id'];

if($type=="A"){
$sql_query3="INSERT INTO product (product_name, quantity, price,shop_id) VALUES ('$product_name','$quantity','$price','$id')";
if(mysqli_query($con,$sql_query3))
{
echo"Data insertion success...";
}
}
else
{
	$sql_query3="UPDATE product SET product_name='$product_name' ,quantity = '$quantity' , price = '$price'where shop_id like '$id' and product_name like '$pname'";
if($con->query($sql_query3)===TRUE)
{
echo"Data insertion success...";
}
}
?>