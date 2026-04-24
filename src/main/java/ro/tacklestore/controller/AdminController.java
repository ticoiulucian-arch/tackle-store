package ro.tacklestore.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ro.tacklestore.repository.CategoryRepository;
import ro.tacklestore.repository.CustomerRepository;
import ro.tacklestore.repository.OrderRepository;
import ro.tacklestore.repository.ProductRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final Cloudinary cloudinary;

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return Map.of(
                "totalProducts", productRepository.count(),
                "totalCategories", categoryRepository.count(),
                "totalOrders", orderRepository.count(),
                "totalCustomers", customerRepository.count()
        );
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder", "tackle-store",
                "resource_type", "image"
        ));
        String url = (String) result.get("secure_url");
        String publicId = (String) result.get("public_id");
        return Map.of("url", url, "publicId", publicId);
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/upload-multiple")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Map<String, String>> uploadMultipleImages(@RequestParam("files") MultipartFile[] files) throws IOException {
        List<Map<String, String>> urls = new java.util.ArrayList<>();
        for (MultipartFile file : files) {
            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "tackle-store",
                    "resource_type", "image"
            ));
            String url = (String) result.get("secure_url");
            String publicId = (String) result.get("public_id");
            urls.add(Map.of("url", url, "publicId", publicId));
        }
        return urls;
    }
}
