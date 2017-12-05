<?php
require"init.php";

$response=array();
$sql_qury4="SELECT * FROM product";
$result2=mysqli_query($con,$sql_qury2);
if($row2=mysqli_fetch_assoc($result2))
{
	$name=$row2['product_name'];
	array_push($name,array("product_name",$name));
}
echo json_encode($response)
    
}
?>