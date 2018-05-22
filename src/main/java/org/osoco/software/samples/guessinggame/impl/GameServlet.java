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
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardContextSelect;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardServletPattern;
import org.osoco.software.samples.guessinggame.Game;
import org.osoco.software.samples.guessinggame.GameController;
import org.osoco.software.samples.guessinggame.HighscoreService;
import org.osoco.software.samples.guessinggame.Level;
import org.osoco.software.samples.guessinggame.Score;

@Component(service = Servlet.class)
@HttpWhiteboardServletPattern("/game")
@HttpWhiteboardContextSelect("(" + HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME +"=" + AppServletContext.NAME + ")")
public class GameServlet extends HttpServlet {

	private static final long serialVersionUID = -2382280784117030938L;

	@Reference
	private GameController game;
	
	@Reference
	private HighscoreService highscore;
	
	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
	throws ServletException, IOException {
	    HttpSession session = req.getSession(false);
	    Game status = null;
	    final Object obj = (session != null ? session.getAttribute("status") : null);
	    if ( obj != null ) {
	    	if ( obj instanceof Game ) {
	    		status = (Game)obj;
	    	} else {
	    		session.removeAttribute("status");
	    	}
	    }
	    
	    resp.setContentType("text/html");
	    resp.setCharacterEncoding("UTF-8");
	    final PrintWriter pw = resp.getWriter();
	    pw.println("<html>");
	    pw.println("<head>");
	    pw.println("<title>Guessing Game</title>");
	    pw.println("</head>");
	    pw.println("<body>");
	    pw.println("<h1>Welcome to the OSGi Guessing Game</h1>");
	    if ( status != null ) {
	    	printStatus(req, pw, status);
	    } else {
	    	printForm(req, pw);
	    }
	    pw.println("</body>");
	    pw.println("</html>");
	}

	private void printStatus(final HttpServletRequest req, final PrintWriter pw, final Game status) {
		final Integer lastGuess = (Integer) req.getSession().getAttribute("guess");
		
		boolean doForm = true;
		if ( lastGuess == null ) {
			pw.print("<p>Make a guess...the number is between 1 and ");
			pw.print(String.valueOf(game.getMax(status.getLevel())));
			pw.println("</p>");
		} else {
			final int result = this.game.nextGuess(status, lastGuess);
			if ( result == 0 ) {
				doForm = false;
				pw.println("<p>Congrats! You won.</p>");
				
				final int ranking = this.highscore.addScore(status.getLevel(), new Score(status.getName(), status.getAttempts()));
				
				if ( ranking == -1 ) {
					pw.println("<p>You missed the highscore list</p>");
				} else {
					pw.print("<p>You're now number ");
					pw.print(String.valueOf(ranking+1));
					pw.println(" in the highscore list</p>");
				}
				pw.println("<table><tbody>");
				for(final Score s : highscore.getHighscores(status.getLevel())) {
					pw.print("<tr><td>");
					pw.print(s.getName());
					pw.print("</td><td><b>");
					pw.print(String.valueOf(s.getAttempts()));
					pw.print("</b></td><td>");
					final DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
					pw.print(df.format(s.getDate()));
					pw.println("</td></tr>");
				}
				pw.println("</tbody></table>");
				pw.println("<br/>");
				pw.print("<p> Try <a href=\"");
				pw.print(req.getContextPath());
				pw.print(req.getServletPath());
				pw.println("\">again</a></p>");
				req.getSession().removeAttribute("guess");
				req.getSession().removeAttribute("status");
			} else {
				pw.print("<p>You're close...your guess was too ");
				if ( result == -1 ) {
					pw.print("low");
				} else {
					pw.print("high");
				}
				pw.println("...try again...</p>");
				pw.print("<p>Number of attempts: ");
				pw.print(String.valueOf(status.getAttempts()));
				pw.println("</p>");
			}
		}
		if ( doForm ) {
		    pw.print("<form method=\"POST\" action=\"");
		    pw.print(req.getContextPath());
		    pw.print(req.getServletPath());
		    pw.println("\">");
		    pw.println("<label for=\"guess\">Guess: </label><input type=\"text\" name=\"guess\"/><br/>");
		    pw.println("<br/>");
		    pw.println("<input type=\"submit\" value=\"Go!\"/>");
		    pw.println("</form>");		
	    }
	}
	
	private void printForm(final HttpServletRequest req, final PrintWriter pw) {
	    pw.println("<p>Type in your name, select a level and start the game:</p>");
	    pw.print("<form method=\"POST\" action=\"");
	    pw.print(req.getContextPath());
	    pw.print(req.getServletPath());
	    pw.println("\">");
	    pw.println("<label for=\"name\">Name: </label><input type=\"text\" name=\"name\"/><br/>");
	    pw.println("<label for=\"level\">Level: </label><select name=\"level\" size=\"1\">");
	    pw.print("<option value=\"");
	    pw.print(Level.EASY.name());
	    pw.println("\">Easy</option>");
	    pw.print("<option value=\"");
	    pw.print(Level.MEDIUM.name());
	    pw.println("\">Medium</option>");
	    pw.print("<option value=\"");
	    pw.print(Level.HARD.name());
	    pw.println("\">Hard</option>");
	    pw.println("</select>");
	    pw.println("<br/>");
	    pw.println("<input type=\"submit\" value=\"Go!\"/>");
	    pw.println("</form>");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	    final HttpSession session = req.getSession();
	    Game status = (session != null ? (Game)session.getAttribute("status") : null);
	    if ( status == null ) {
	        final Level level = Level.valueOf(req.getParameter("level"));
	        final String user = req.getParameter("name");
	        
	        status = game.startGame(user, level);
	        session.setAttribute("status", status);
	    } else {	    	
	    	final int guess = Integer.valueOf(req.getParameter("guess"));
	    	session.setAttribute("guess", guess);
	    }
	    resp.sendRedirect(req.getContextPath() + req.getServletPath());
	}
}
