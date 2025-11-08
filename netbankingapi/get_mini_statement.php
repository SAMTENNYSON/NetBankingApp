<?php
header("Content-Type: application/json; charset=utf-8");
error_reporting(0);

$host = "localhost";
$db_name = "netbanking";
$db_user = "root";
$db_pass = "";

$conn = new mysqli($host, $db_user, $db_pass, $db_name);
if ($conn->connect_error) {
    echo json_encode(["success" => false, "message" => "DB connection failed"]);
    exit;
}

$raw = trim(file_get_contents("php://input"));
$data = json_decode($raw, true);
$cif = $data['cif_number'] ?? '';

if ($cif === '') {
    echo json_encode(["success" => false, "message" => "Missing CIF number"]);
    exit;
}

// fetch last 5 transactions for this CIF (most recent first)
$sql = "SELECT id, type, amount, description, DATE_FORMAT(date, '%Y-%m-%d %H:%i:%s') as date
        FROM transactions
        WHERE cif_number = '$cif'
        ORDER BY date DESC
        LIMIT 5";

$result = $conn->query($sql);
$items = [];

if ($result) {
    while ($row = $result->fetch_assoc()) {
        $items[] = $row;
    }
    echo json_encode(["success" => true, "transactions" => $items], JSON_UNESCAPED_UNICODE);
} else {
    echo json_encode(["success" => false, "message" => "Query failed"]);
}

$conn->close();
