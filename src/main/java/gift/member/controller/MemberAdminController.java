package gift.member.controller;

import gift.member.dto.MemberRegisterRequestDto;
import gift.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/members")
public class MemberAdminController {
    private final MemberService memberService;

    public MemberAdminController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("members", memberService.findAll());
        return "members/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("member", MemberRegisterRequestDto.from());
        return "members/create_form";
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id, Model model) {
        model.addAttribute("member", memberService.findById(id));
        return "members/update_form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute MemberRegisterRequestDto requestDto) {
        memberService.register(requestDto);
        return "redirect:/admin/members";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute MemberRegisterRequestDto requestDto) {
        memberService.update(id, requestDto);
        return "redirect:/admin/members";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        memberService.delete(id);
        return "redirect:/admin/members";
    }
}
