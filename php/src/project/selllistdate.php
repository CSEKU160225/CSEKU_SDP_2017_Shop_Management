<?php
require"init.php";

$email=$_POST["Email"];
$date=$_POST["date"];

// $email="Mehadi";
// $date="2017-11-24";

$sql="SELECT DATE('$date') AS odate";
$result=mysqli_query($con,$sql);
$row=$result->fetch_assoc();
$dateid=$row['odate'];

$sql="SELECT * from registration where Email like '$email'";
$result=mysqli_query($con,$sql);
$row=mysqli_fetch_assoc($result);
$person_id=$row['res_id'];

$sql_qury2="SELECT * FROM shop WHERE res_id like '$person_id'";
$result2=mysqli_query($con,$sql_qury2);
if($row2=mysqli_fetch_assoc($result2))
	$id=$row2['shop_id'];

$response= array();

$sql_query3="SELECT * from sell where shop_id like '$id' and DATE(sell_date) = '$dateid'";
$result3=mysqli_query($con,$sql_query3);
while($row3=mysqli_fetch_assoc($result3))
{
	array_push($response, array("product"=>$row3['product_name'],"quantity"=>$row3['sell_quantity'],"price"=>$row3['total_sell_price'],"date"=>$row3['sell_date']));
}
echo json_encode(array("Server_response"=>$response));

mysqli_close($con);
?>