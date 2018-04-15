package hello;

import com.github.tomciaaa.docker_hub_api.TheWholeShebang;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class HelloController {
    @GetMapping("/pull")
    public void pullImage(HttpServletResponse response, String image) throws Exception {
        Process command = new ProcessBuilder().command("/usr/bin/sudo", "docker", "pull", image).start();

        response.setContentType("txt/plain");

        // Copy the stream to the response's output stream.
        IOUtils.copy(command.getInputStream(), response.getOutputStream());
        command.waitFor();
        response.flushBuffer();
    }

    @GetMapping("/remove")
    public void removeImage(HttpServletResponse response, String image) throws Exception {
        Process command = new ProcessBuilder().command("/usr/bin/sudo", "docker", "rmi", image).start();

        response.setContentType("txt/plain");

        // Copy the stream to the response's output stream.
        IOUtils.copy(command.getInputStream(), response.getOutputStream());
        command.waitFor();
        response.flushBuffer();
    }

    @GetMapping("/save")
    public void testSream(HttpServletResponse response, String image) throws Exception {
        Process command = new ProcessBuilder().command("/usr/bin/sudo", "docker", "save", image).start();
        //command.getInputStream();
        // Set the content type and attachment header.
        response.addHeader("Content-disposition", "attachment;filename="+image.replaceAll("\\W", "-")+".tar");
        response.setContentType("application/x-tar");

        // Copy the stream to the response's output stream.
        IOUtils.copy(command.getInputStream(), response.getOutputStream());
        command.waitFor();
        response.flushBuffer();
    }

    @GetMapping("/")
    public void downloadDirect(HttpServletResponse response, String repository, String tag) throws Exception {
        assert repository != null && !repository.isEmpty();
        assert tag != null && !tag.isEmpty();
        response.addHeader("Content-disposition", "attachment;filename="+(repository+":"+tag).replaceAll("\\W", "-")+".tar");
        response.setContentType("application/x-tar");

        TheWholeShebang.FetchImage(repository, tag, response.getOutputStream());
        response.flushBuffer();

    }
}
