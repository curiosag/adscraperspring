package app.server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.Const;

@Controller
public class WebConsoleController {

	private final CommandHandler handler;
	
	public WebConsoleController(CommandHandler handler) {
		this.handler = handler;
	}
	
    @RequestMapping("/console")
    public String greeting(@RequestParam(value="cmd", required=true, defaultValue=Const.NOCMD) String cmd, Model model) {
    	
        model.addAttribute("consoleReply", handler.hdlCmd(cmd));
        return "console";
    }

}