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

// Fetch profile details
$sql = "SELECT account_number, cif_number, branch_code, country, mobile_number FROM users WHERE cif_number = '$cif_number'";
$result = $conn->query($sql);

if ($result && $row = $result->fetch_assoc()) {
    echo json_encode([
        "success" => true,
        "account_number" => $row['account_number'],
        "cif_number" => $row['cif_number'],
        "branch_code" => $row['branch_code'],
        "country" => $row['country'],
        "mobile_number" => $row['mobile_number']
    ]);
} else {
    echo json_encode(["success" => false, "message" => "User not found"]);
}

$conn->close();
?>
