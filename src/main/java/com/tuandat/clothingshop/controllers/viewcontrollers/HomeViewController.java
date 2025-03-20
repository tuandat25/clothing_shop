package com.tuandat.clothingshop.controllers.viewcontrollers;

import java.nio.file.AccessDeniedException;
import java.nio.file.FileSystemException;
import java.security.Principal;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tuandat.clothingshop.models.User;
import com.tuandat.clothingshop.services.category.ICategoryService;
import com.tuandat.clothingshop.services.order.IOrderService;
import com.tuandat.clothingshop.services.product.IProductService;
import com.tuandat.clothingshop.services.role.IRoleService;
import com.tuandat.clothingshop.services.user.IUserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/4season")
public class HomeViewController {

    private final ICategoryService categoryService;
    private final IProductService productService;
    private final IUserService userService;
    private final IOrderService orderService;
    private final IRoleService roleService;

    @GetMapping("/home")
    public String index(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "home/index";
    }

    @GetMapping("/admin")
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userService.count());
        model.addAttribute("totalProducts", productService.count());
        model.addAttribute("totalOrders", orderService.count());
        model.addAttribute("totalCategories", categoryService.count());
        return "admin/index";
    }

    @GetMapping("/admin/userList")
    public String userList(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        // Thymeleaf sẽ render file templates/admin/userList.html
        return "admin/userList";
    }

    @GetMapping("/admin/edit/{id}")
    public String editUser(@PathVariable UUID id, Principal principal, Model model) throws FileSystemException {
        // Lấy thông tin người dùng hiện tại từ SecurityContext (hoặc từ Principal)
        User loggedInUser = (User) ((Authentication) principal).getPrincipal();
        // Kiểm tra nếu người dùng không phải admin, thì chỉ cho phép sửa thông tin của
        // chính họ
        String roleName = loggedInUser.getRole().getName();
        if (!(roleName.equalsIgnoreCase("ADMIN") || roleName.equalsIgnoreCase("ROLE_ADMIN"))
                && !loggedInUser.getId().equals(id)) {
            throw new AccessDeniedException("Bạn không có quyền xem trang này.");
        }

        // Nếu hợp lệ, load thông tin người dùng cần chỉnh sửa
        var userToEdit = userService.getById(id);
        model.addAttribute("user", userToEdit);
        return "admin/userEdit"; // Thymeleaf sẽ render templates/admin/userEdit.html
    }

    // // Xử lý form submit từ trang chỉnh sửa thông tin người dùng
    // @PostMapping("/admin/edit/{id}")
    // public String editUser(@PathVariable UUID id, User user, RedirectAttributes redirectAttributes) {
    //     // Lưu thông tin người dùng mới vào CSDL
    //     userService.save(user);
    //     // Redirect về trang danh sách người dùng
    //     redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin người dùng thành công.");
    //     return "redirect:/4season/admin/userList";
    // }

}
