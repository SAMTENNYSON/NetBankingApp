<?php
$host = "localhost";
$db_name = "netbanking";
$db_user = "root";
$db_pass = "";

$conn = new mysqli($host, $db_user, $db_pass, $db_name);

if ($conn->connect_error) {
    die(json_encode(["success" => false, "message" => "DB connection failed"]));
}

$data = json_decode(file_get_contents("php://input"));
$cif_number = $data->cif_number;

$sql = "SELECT type, amount, date FROM transactions WHERE cif_number='$cif_number' ORDER BY date DESC";
$result = $conn->query($sql);

$transactions = [];
if ($result) {
    while ($row = $result->fetch_assoc()) {
        $transactions[] = $row;
    }
    echo json_encode(["success" => true, "transactions" => $transactions]);
} else {
    echo json_encode(["success" => false, "message" => "No transactions found"]);
}

$conn->close();
?>
