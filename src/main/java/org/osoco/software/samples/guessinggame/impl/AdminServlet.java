/*
 * This file is licensed to you under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except 
 * in compliance with the License.  You may obtain a copy of the 
 * License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.osoco.software.samples.guessinggame.impl;


import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osoco.software.samples.guessinggame.Game;
import org.osoco.software.samples.guessinggame.GameController;
import org.osoco.software.samples.guessinggame.HighscoreService;
import org.osoco.software.samples.guessinggame.Level;
import org.osoco.software.samples.guessinggame.Score;

@Component(service = Servlet.class,
           property = { HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN + "=/admin",
        		        HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT + "=(" + HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME +"=" + AppServletContext.NAME + ")"})
public class AdminServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Reference
	private HighscoreService highscore;
	
	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
	throws ServletException, IOException {
	    resp.setContentType("text/html");
	    resp.setCharacterEncoding("UTF-8");
	    final PrintWriter pw = resp.getWriter();
	    pw.println("<html>");
	    pw.println("<head>");
	    pw.println("<title>Guessing Game Admin</title>");
	    pw.println("</head>");
	    pw.println("<body>");
	    pw.println("<h1>Welcome to the OSGi Guessing Game Admin Console</h1>");
	    pw.println("</body>");
	    pw.println("</html>");
	}

}
