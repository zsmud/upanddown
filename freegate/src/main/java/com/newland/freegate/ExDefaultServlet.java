package com.newland.freegate;

import org.apache.catalina.WebResource;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.tomcat.util.security.Escape;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by wot_zhengshenming on 2021/6/1.
 */
public class ExDefaultServlet extends DefaultServlet {

    /**
     * 删除文件资源时候为了让它停留在父目录中页中
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException, ServletException {

        // Identify the requested resource path
        String path = getRelativePath(request, true);

        WebResource resource = resources.getResource(path);

        //删除资源时候让它停留在父目录中
        if(resource.exists()&&resource.isFile()){
            String delete = request.getParameter("delete");
            if(delete!=null&&resource.delete()){
                String parentDirectory = resource.getWebappPath();
                if (parentDirectory.endsWith("/")) {
                    parentDirectory =
                            parentDirectory.substring(0, parentDirectory.length() - 1);
                }
                int slash = parentDirectory.lastIndexOf('/');
                if (slash >= 0) {
                    String parent = resource.getWebappPath().substring(0, slash);
                    request.getRequestDispatcher(parent).forward(request, response);
                }else{
                    request.getRequestDispatcher("/").forward(request, response);
                }
                return;
            }
        }


        // Serve the requested resource, including the data content
        serveResource(request, response, true, fileEncoding);

    }

    /**
     * 重写原来的显示页面，1.去掉服务器信息，2.加一列删除列，3.加返回上传的连接地址
     * @param contextPath
     * @param resource
     * @param encoding
     * @return
     * @throws IOException
     */
    protected InputStream renderHtml(String contextPath, WebResource resource, String encoding)
            throws IOException {

        // Prepare a writer to a buffered area
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter osWriter = new OutputStreamWriter(stream, "UTF8");
        PrintWriter writer = new PrintWriter(osWriter);

        StringBuilder sb = new StringBuilder();

        String[] entries = resources.list(resource.getWebappPath());

        // rewriteUrl(contextPath) is expensive. cache result for later reuse
        String rewrittenContextPath =  rewriteUrl(contextPath);
        String directoryWebappPath = resource.getWebappPath();

        // Render the page header
        sb.append("<html>\r\n");
        sb.append("<head>\r\n");
        sb.append("<title>");
        sb.append(sm.getString("directory.title", directoryWebappPath));
        sb.append("</title>\r\n");
        sb.append("<STYLE><!--");
        sb.append(org.apache.catalina.util.TomcatCSS.TOMCAT_CSS);
        sb.append("--></STYLE> ");
        //add return to upload href
        sb.append("<script language=\"javascript\">");
        sb.append("function gotoUpload(){");
        sb.append("	var webpath = \""+directoryWebappPath+"\";");
        sb.append("	var currentHref = window.location.href;");
        sb.append("	var uploadurl = currentHref.substring(0,currentHref.indexOf(webpath));");
        sb.append("	window.location = uploadurl");
        sb.append("}");
        sb.append("</script>");
        sb.append("</head>\r\n");
        sb.append("<body>");
        sb.append("<h1>");
        sb.append(sm.getString("directory.title", directoryWebappPath));

        // Render the link to our parent (if required)
        String parentDirectory = directoryWebappPath;
        if (parentDirectory.endsWith("/")) {
            parentDirectory =
                    parentDirectory.substring(0, parentDirectory.length() - 1);
        }
        int slash = parentDirectory.lastIndexOf('/');
        if (slash >= 0) {
            String parent = directoryWebappPath.substring(0, slash);
            sb.append(" - <a href=\"");
            //sb.append(rewrittenContextPath);
            if (parent.equals(""))
                parent = "/";
            sb.append("..");
            if (!parent.endsWith("/"))
                sb.append("/");
            sb.append("\">");
            sb.append("<b>");
            sb.append(sm.getString("directory.parent", parent));
            sb.append("</b>");
            sb.append("</a>");
        }
        sb.append("&nbsp;&nbsp;<a href=\"#\" onclick=\"gotoUpload();\"><font color=\"#FFFFFF\">upload</font></a>");
        sb.append("</h1>");
        sb.append("<HR size=\"1\" noshade=\"noshade\">");

        sb.append("<table width=\"100%\" cellspacing=\"0\"" +
                " cellpadding=\"5\" align=\"center\">\r\n");

        // Render the column headings
        sb.append("<tr>\r\n");
        sb.append("<td align=\"left\"><font size=\"+1\"><strong>");
        sb.append(sm.getString("directory.filename"));
        sb.append("</strong></font></td>\r\n");
        sb.append("<td align=\"center\"><font size=\"+1\"><strong>");
        sb.append(sm.getString("directory.size"));
        sb.append("</strong></font></td>\r\n");
        sb.append("<td align=\"right\"><font size=\"+1\"><strong>");
        sb.append(sm.getString("directory.lastModified"));
        sb.append("</strong></font></td>\r\n");
        sb.append("</tr>");

        boolean shade = false;
        for (String entry : entries) {
            if (entry.equalsIgnoreCase("WEB-INF") ||
                    entry.equalsIgnoreCase("META-INF"))
                continue;

            WebResource childResource =
                    resources.getResource(directoryWebappPath + entry);
            if (!childResource.exists()) {
                continue;
            }

            sb.append("<tr");
            if (shade)
                sb.append(" bgcolor=\"#eeeeee\"");
            sb.append(">\r\n");
            shade = !shade;

            sb.append("<td align=\"left\">&nbsp;&nbsp;\r\n");
            sb.append("<a href=\"");
            //sb.append(rewrittenContextPath);
            sb.append(rewriteUrl(entry));
            if (childResource.isDirectory())
                sb.append("/");
            sb.append("\"><tt>");
            sb.append(Escape.htmlElementContent(entry));
            if (childResource.isDirectory())
                sb.append("/");
            sb.append("</tt></a></td>\r\n");

            sb.append("<td align=\"right\"><tt>");
            if (childResource.isDirectory())
                sb.append("&nbsp;");
            else
                sb.append(renderSize(childResource.getContentLength()));
            sb.append("</tt></td>\r\n");

            sb.append("<td align=\"right\"><tt>");
            sb.append(childResource.getLastModifiedHttp());
            sb.append("</tt></td>\r\n");

            sb.append("<td align=\"right\"><tt>");
            if (childResource.isFile()) {
                sb.append("<a href=\"");
                sb.append(rewriteUrl(entry) + "?delete");
                sb.append("\">delete</a>");
            }
            sb.append("</tt></td>\r\n");

            sb.append("</tr>\r\n");
        }

        // Render the page footer
        sb.append("</table>\r\n");

        sb.append("<HR size=\"1\" noshade=\"noshade\">");

        String readme = getReadme(resource, encoding);
        if (readme!=null) {
            sb.append(readme);
            sb.append("<HR size=\"1\" noshade=\"noshade\">");
        }


        sb.append("</body>\r\n");
        sb.append("</html>\r\n");

        // Return an input stream to the underlying bytes
        writer.write(sb.toString());
        writer.flush();
        return new ByteArrayInputStream(stream.toByteArray());

    }
}
