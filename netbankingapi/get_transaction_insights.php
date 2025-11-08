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

// Get category-wise spending (only debit transactions for expenses)
$sql = "SELECT 
            COALESCE(category, 'Other') as category,
            SUM(amount) as total_amount,
            COUNT(*) as transaction_count
        FROM transactions 
        WHERE cif_number = '$cif_number' 
        AND type = 'debit'
        GROUP BY category
        ORDER BY total_amount DESC";

$result = $conn->query($sql);

$insights = [];
$totalExpense = 0;

if ($result) {
    while ($row = $result->fetch_assoc()) {
        $insights[] = [
            "category" => $row['category'],
            "total_amount" => floatval($row['total_amount']),
            "transaction_count" => intval($row['transaction_count'])
        ];
        $totalExpense += floatval($row['total_amount']);
    }
    
    // Get monthly trend data (last 6 months)
    $monthlyTrend = [];
    for ($i = 5; $i >= 0; $i--) {
        $month = date('Y-m', strtotime("-$i months"));
        $monthSql = "SELECT SUM(amount) as total 
                     FROM transactions 
                     WHERE cif_number = '$cif_number' 
                     AND type = 'debit'
                     AND DATE_FORMAT(date, '%Y-%m') = '$month'";
        $monthResult = $conn->query($monthSql);
        $monthRow = $monthResult->fetch_assoc();
        $monthlyTrend[] = [
            "month" => date('M Y', strtotime("-$i months")),
            "total" => floatval($monthRow['total'] ?? 0)
        ];
    }
    
    echo json_encode([
        "success" => true,
        "insights" => $insights,
        "total_expense" => $totalExpense,
        "monthly_trend" => $monthlyTrend
    ]);
} else {
    echo json_encode(["success" => false, "message" => "Failed to fetch insights"]);
}

$conn->close();
?>

