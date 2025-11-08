<?php
header("Content-Type: application/json");

$host = "localhost";
$db_name = "netbanking";
$db_user = "root";
$db_pass = "";

$conn = new mysqli($host, $db_user, $db_pass, $db_name);

if ($conn->connect_error) {
    echo json_encode(["success" => false, "message" => "DB connection failed"]);
    exit;
}

// Read raw input
$data = json_decode(file_get_contents("php://input"));

// Defensive check
if (!$data) {
    echo json_encode(["success" => false, "message" => "Invalid JSON input"]);
    exit;
}

$account_number   = $conn->real_escape_string($data->account_number ?? "");
$cif_number       = $conn->real_escape_string($data->cif_number ?? "");
$branch_code      = $conn->real_escape_string($data->branch_code ?? "");
$country          = $conn->real_escape_string($data->country ?? "");
$mobile_number    = $conn->real_escape_string($data->mobile_number ?? "");
$password         = $conn->real_escape_string($data->password ?? "");
$transaction_pin  = $conn->real_escape_string($data->transaction_pin ?? "");

// Uniqueness checks
$check = $conn->query("SELECT * FROM users WHERE account_number='$account_number' OR cif_number='$cif_number'");
if ($check && $check->num_rows > 0) {
    echo json_encode(["success" => false, "message" => "Account Number or CIF already exists"]);
    exit;
}

// Insert user
$sql = "INSERT INTO users (account_number, cif_number, branch_code, country, mobile_number, password, balance, transaction_pin)
        VALUES ('$account_number', '$cif_number', '$branch_code', '$country', '$mobile_number', '$password', 0, '$transaction_pin')";

if ($conn->query($sql) === TRUE) {
    echo json_encode(["success" => true, "message" => "Registration successful"]);
} else {
    echo json_encode(["success" => false, "message" => "Error: " . $conn->error]);
}

$conn->close();
?>
