<?php
require"init.php";

$response=array();
$sql_query3="SELECT * from product";
$result3=mysqli_query($con,$sql_query3);
while($row3=mysqli_fetch_assoc($result3))
{
	array_push($response, array("product_name"=>$row3['product_name']));
}
echo json_encode(array("Server_response"=>$response));

mysqli_close($con);
    
?>