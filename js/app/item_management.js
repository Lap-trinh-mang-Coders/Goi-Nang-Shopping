$(document).ready(function () {
    // --- KHAI BÁO BIẾN ---
    const API_URL = 'http://localhost:2025/api/items';
    const token = localStorage.getItem('accessToken');
    const itemTableBody = $('#itemsTable tbody');
    const itemModal = $('#itemModal');
    const itemForm = $('#itemForm');

    // --- KIỂM TRA ĐĂNG NHẬP ---
    // Phải được đặt ở ngay đầu để bảo vệ trang
    if (!token) {
        // Chuyển về trang login nếu chưa đăng nhập
        window.location.href = 'login3.html';
        return; // Dừng chạy code nếu chưa đăng nhập
    }

    // --- CÁC HÀM XỬ LÝ ---

    // Hàm tải danh sách sản phẩm
    function loadItems() {
        $.get(API_URL, function (data) {
            itemTableBody.empty();
            data.forEach(item => {
                itemTableBody.append(`
                    <tr data-id="${item.id}">
                        <td>${item.id}</td>
                        <td>${item.name}</td>
                        <td>${new Intl.NumberFormat('vi-VN').format(item.price)} VND</td>
                        <td>${item.category}</td>
                        <td><label class="badge ${item.available ? 'badge-success' : 'badge-danger'}">${item.available ? 'Còn hàng' : 'Hết hàng'}</label></td>
                        <td>
                            <button class="btn btn-sm btn-info edit-btn">Sửa</button>
                            <button class="btn btn-sm btn-danger delete-btn">Xóa</button>
                        </td>
                    </tr>
                `);
            });
        });
    }

    // Hàm reset form
    function resetForm() {
        itemForm[0].reset();
        $('#itemId').val('');
        itemModal.find('.modal-title').text('Thêm sản phẩm mới');
    }

    // --- CÁC SỰ KIỆN (EVENT LISTENERS) ---

    // Sự kiện click nút "Thêm mới"
    $('#addNewItemBtn').on('click', function () {
        resetForm();
    });

    // Sự kiện click nút "Lưu" (cho cả Thêm và Sửa)
    $('#saveItemBtn').on('click', function () {
        const itemData = {
            name: $('#itemName').val(),
            description: $('#itemDescription').val(),
            price: parseFloat($('#itemPrice').val()),
            category: $('#itemCategory').val(),
            available: $('#itemAvailable').is(':checked'),
            discount: 0,
            imageUrl: ''
        };

        const itemId = $('#itemId').val();
        const method = itemId ? 'PUT' : 'POST';
        const url = itemId ? `${API_URL}/${itemId}` : API_URL;

        // BƯỚC KIỂM TRA QUAN TRỌNG
        console.log("Đang gửi token:", token);

        $.ajax({
            url: url,
            method: method,
            contentType: 'application/json',
            data: JSON.stringify(itemData),
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function () {
                itemModal.modal('hide');
                loadItems();
            },
            error: function (err) {
                alert('Thao tác thất bại! Bạn không có quyền thực hiện việc này.');
                console.error('Lỗi khi lưu item:', err);
            }
        });
    });

    // Sự kiện click nút "Sửa"
    itemTableBody.on('click', '.edit-btn', function () {
        resetForm();
        const itemId = $(this).closest('tr').data('id');
        $.get(`${API_URL}/${itemId}`, function (item) {
            $('#itemId').val(item.id);
            $('#itemName').val(item.name);
            $('#itemDescription').val(item.description);
            $('#itemPrice').val(item.price);
            $('#itemCategory').val(item.category);
            $('#itemAvailable').prop('checked', item.available);
            itemModal.find('.modal-title').text('Chỉnh sửa sản phẩm');
            itemModal.modal('show');
        });
    });

    // Sự kiện click nút "Xóa"
    itemTableBody.on('click', '.delete-btn', function () {
        const itemId = $(this).closest('tr').data('id');
        if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')) {
            $.ajax({
                url: `${API_URL}/${itemId}`,
                method: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + token
                },
                success: function () {
                    loadItems();
                },
                error: function (err) {
                    alert('Thao tác thất bại! Bạn không có quyền thực hiện việc này.');
                    console.error('Lỗi khi xóa item:', err);
                }
            });
        }
    });

    // Sự kiện click nút "Logout"
    $('#logoutBtn').on('click', function () {
        localStorage.removeItem('accessToken');
        window.location.href = 'login3.html';
    });

    // --- KHỞI CHẠY ---
    loadItems(); // Tải danh sách sản phẩm lần đầu
});