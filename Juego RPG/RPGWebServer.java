import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class RPGWebServer {

    private static HeroCharacter hero;

    public static void main(String[] args) throws IOException {
        hero = new BaseHero("Cyber Guerrero", "🥷", 150, 20, 25, 10);

        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/", new MainMenuHandler());
        server.createContext("/equip", new DecoratorHandler());
        server.createContext("/combat", new CombatHandler());
        server.createContext("/reset", new ResetHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("Servidor Web HTTP RPG Dinámico iniciado en http://localhost:8081");
        System.out.println("Renderizando interfaz animada interactiva (WASD) desde Java...");
    }

    // ============================================
    // MANEJADORES DE RUTAS WEB
    // ============================================
    
    static class MainMenuHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String ui = renderizarMenuPrincipal(hero);
            byte[] bytes = ui.getBytes(StandardCharsets.UTF_8);
            t.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            t.sendResponseHeaders(200, bytes.length);
            OutputStream os = t.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }

    static class DecoratorHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            if ("POST".equalsIgnoreCase(t.getRequestMethod())) {
                InputStream is = t.getRequestBody();
                byte[] requestBytes = new byte[is.available()];
                is.read(requestBytes);
                String body = new String(requestBytes, StandardCharsets.UTF_8).replace("item=", "");

                // POLIMORFISMO DECORATOR
                if (body.equals("Espada")) hero = new WeaponDecorator(hero, "Plasma Blade", 30, 0);
                if (body.equals("Armadura")) hero = new ArmorDecorator(hero, "Exo-Armadura", 40, 20);
                if (body.equals("Poder")) hero = new PowerDecorator(hero, "Rayo Eldritch", 40, 10);
                if (body.equals("Buff")) hero = new BuffDecorator(hero, "Poción Adrenalina", 15);

                t.getResponseHeaders().add("Location", "/");
                t.sendResponseHeaders(302, -1);
            }
        }
    }

    static class ResetHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            if ("POST".equalsIgnoreCase(t.getRequestMethod())) {
                InputStream is = t.getRequestBody();
                byte[] requestBytes = new byte[is.available()];
                is.read(requestBytes);
                String body = new String(requestBytes, StandardCharsets.UTF_8).replace("clase=", "");
                
                if (body.equals("Guerrero")) hero = new BaseHero("Cyber Guerrero", "🥷", 150, 20, 25, 10);
                else if (body.equals("Mago")) hero = new BaseHero("Mago del Vacío", "🧙‍♂️", 100, 35, 10, 15);
                else if (body.equals("Asesino")) hero = new BaseHero("Asesino Neón", "🗡️", 110, 30, 12, 30);
                else hero = new BaseHero("Cyber Guerrero", "🥷", 150, 20, 25, 10);

                t.getResponseHeaders().add("Location", "/");
                t.sendResponseHeaders(302, -1);
            }
        }
    }

    // ============================================
    // SISTEMA DE COMBATE INTERACTIVO Y NATIVO (WASD)
    // ============================================

    static class CombatHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            int bossMaxHp = 800; // Un jefe poderoso para jugar en vivo
            int heroMaxHp = hero.getHp();
            
            // Renderizamos la arena enviando las estadisticas directas
            String ui = renderizarArenaInteractiva(hero, bossMaxHp, heroMaxHp);
            byte[] bytes = ui.getBytes(StandardCharsets.UTF_8);
            t.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            t.sendResponseHeaders(200, bytes.length);
            OutputStream os = t.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }


    // ============================================
    // CONSTRUCTORES DE HTML NATIVOS DE JAVA
    // ============================================

    private static String renderizarMenuPrincipal(HeroCharacter current) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'><title>RPG EDGAR</title>");
        html.append("<style>");
        html.append("body { background: #0b0c10; color: #c5c6c7; font-family: 'Segoe UI', Tahoma, Verdana, sans-serif; margin: 0; padding: 20px; overflow-x: hidden; }");
        html.append("h1, h2, h3 { color: #66fcf1; text-align: center; text-transform: uppercase; letter-spacing: 2px; text-shadow: 0 0 10px rgba(102,252,241,0.5); }");
        html.append(".container { display: flex; justify-content: center; flex-wrap: wrap; max-width: 1200px; margin: 0 auto; gap: 30px; }");
        html.append(".card { background: #1f2833; border-radius: 15px; padding: 25px; box-shadow: 0 10px 30px rgba(0,0,0,0.5); border-top: 4px solid #45a29e; flex: 1; min-width: 300px; max-width: 500px; transition: transform 0.3s; position: relative; overflow: hidden; }");
        html.append(".card:hover { transform: translateY(-5px); box-shadow: 0 15px 40px rgba(102, 252, 241, 0.2); }");
        
        html.append(".avatar { font-size: 100px; text-align: center; margin: 10px 0; animation: float 3s ease-in-out infinite; filter: drop-shadow(0 0 20px rgba(102,252,241,0.4)); }");
        html.append("@keyframes float { 0% { transform: translateY(0px); } 50% { transform: translateY(-15px); } 100% { transform: translateY(0px); } }");
        
        html.append(".btn { display: block; width: 100%; margin: 12px 0; padding: 15px; background: rgba(31, 40, 51, 0.8); border: 2px solid #45a29e; color: #66fcf1; font-size: 16px; font-weight: bold; border-radius: 8px; cursor: pointer; transition: 0.3s; position: relative; overflow: hidden; }");
        html.append(".btn:hover { background: #45a29e; color: #0b0c10; box-shadow: 0 0 20px #45a29e; transform: scale(1.02); }");
        html.append(".btn-combat { background: #900; border-color: #f00; color: #fff; text-shadow: 1px 1px 5px #000; font-size: 20px; letter-spacing: 2px; }");
        html.append(".btn-combat:hover { background: #f00; box-shadow: 0 0 30px #f00; color: #fff; }");
        
        html.append(".stat-label { display: flex; justify-content: space-between; font-weight: bold; margin-bottom: 2px; margin-top: 10px; font-size: 14px;}");
        html.append(".stat-bar { background: #0b0c10; height: 15px; border-radius: 10px; margin-bottom: 5px; overflow: hidden; border: 1px solid #333; }");
        html.append(".fill { height: 100%; border-radius: 10px; transition: width 0.8s cubic-bezier(0.34, 1.56, 0.64, 1); }");
        html.append(".char-select { display: flex; justify-content: space-between; gap: 10px; }");
        html.append(".char-btn { flex: 1; padding: 12px; font-weight:bold; font-size:14px; background: rgba(0,0,0,0.4); border: 1px solid #45a29e; color: #66fcf1; cursor: pointer; transition: 0.3s; border-radius: 5px; }");
        html.append(".char-btn:hover { background: #45a29e; color: #000; transform: translateY(-3px); box-shadow: 0 5px 15px rgba(69, 162, 158, 0.4); }");
        html.append("</style></head><body>");
        
        html.append("<h1>🔥 RPG EDGAR🔥</h1>");
        html.append("<div class='container'>");
        
        // Panel del Héroe
        html.append("<div class='card'>");
        html.append("<h2>ESTADO DEL HÉROE</h2>");
        
        String avatar = current.getAvatar();
        
        html.append("<div class='avatar'>").append(avatar).append("</div>");
        html.append("<h3>▶ ").append(current.getName()).append(" ◀</h3>");
        html.append("<p style='text-align:center; color:#c5c6c7; font-size:13px; line-height: 1.6; margin-bottom: 20px;'><i>").append(current.getDescription()).append("</i></p>");
        
        int hpPct = Math.min(100, (int)((current.getHp() / 300.0) * 100));
        int dmgPct = Math.min(100, (int)((current.getDamage() / 150.0) * 100));
        int defPct = Math.min(100, (int)((current.getDefense() / 100.0) * 100));
        int spdPct = Math.min(100, (int)((current.getSpeed() / 60.0) * 100));
        
        html.append("<div class='stat-label'><span>❤️ Salud (HP)</span><span>").append(current.getHp()).append("</span></div>");
        html.append("<div class='stat-bar'><div class='fill' style='width:").append(hpPct).append("%; background: linear-gradient(90deg, #f05454, #ff8c8c); box-shadow: 0 0 10px #f05454;'></div></div>");
        
        html.append("<div class='stat-label'><span>⚔️ Poder Ataque (ATQ)</span><span>").append(current.getDamage()).append("</span></div>");
        html.append("<div class='stat-bar'><div class='fill' style='width:").append(dmgPct).append("%; background: linear-gradient(90deg, #fca311, #ffc971); box-shadow: 0 0 10px #fca311;'></div></div>");
        
        html.append("<div class='stat-label'><span>🛡️ Defensa (DEF)</span><span>").append(current.getDefense()).append("</span></div>");
        html.append("<div class='stat-bar'><div class='fill' style='width:").append(defPct).append("%; background: linear-gradient(90deg, #45a29e, #84dcc6); box-shadow: 0 0 10px #45a29e;'></div></div>");
        
        html.append("<div class='stat-label'><span>⚡ Velocidad (VEL)</span><span>").append(current.getSpeed()).append("</span></div>");
        html.append("<div class='stat-bar'><div class='fill' style='width:").append(spdPct).append("%; background: linear-gradient(90deg, #b6e9df, #e0fbf5); box-shadow: 0 0 10px #b6e9df;'></div></div>");
        
        html.append("</div>");
        
        // Panel de Equipamiento
        html.append("<div class='card'>");
        html.append("<h2>🛠️ ARMAMENTO Y MAGIA</h2>");
        
        html.append("<form method='POST' action='/reset'>");
        html.append("<p style='text-align:center; margin:10px 0; color:#c5c6c7; font-size: 14px;'>CAMBIAR PERSONAJE BASE:</p>");
        html.append("<div class='char-select'>");
        html.append("<button class='char-btn' name='clase' value='Guerrero'>🛡️ GUERRERO</button>");
        html.append("<button class='char-btn' name='clase' value='Mago'>🧙‍♂️ MAGO</button>");
        html.append("<button class='char-btn' name='clase' value='Asesino'>🗡️ ASESINO</button>");
        html.append("</div>");
        html.append("</form><hr style='border: 1px solid rgba(255,255,255,0.1); margin: 25px 0;'>");
        
        html.append("<form action='/equip' method='POST'>");
        html.append("<button class='btn' name='item' value='Espada'>🗡️ Equipar: Plasma Blade (+ATQ)</button>");
        html.append("<button class='btn' name='item' value='Armadura'>⚙️ Equipar: Exo-Armadura (+DEF)</button>");
        html.append("<button class='btn' name='item' value='Poder'>☄️ Aprender: Rayo Eldritch (+ATQ, +HP)</button>");
        html.append("<button class='btn' name='item' value='Buff'>🧪 Beber: Poción Adrenalina (+VEL)</button>");
        html.append("</form>");
        
        html.append("<form action='/combat' method='POST' style='margin-top:20px;'><button class='btn btn-combat'>[ JUGAR ARENA 3D ]</button></form>");
        html.append("</div>");
        
        html.append("</div></body></html>");
        return html.toString();
    }

    // ============================================
    // RENDERIZADOR DE ARENA DE COMBATE (INTERACTIVA 100%)
    // ============================================
    private static String renderizarArenaInteractiva(HeroCharacter current, int bossMax, int heroMax) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'><title>Guerra - RPG EDGAR</title>");
        html.append("<style>");
        html.append("body { background: #111; color: white; margin: 0; overflow: hidden; font-family: 'Segoe UI', sans-serif; user-select: none; }");
        html.append("#arena { width: 100vw; height: 100vh; position: relative; background: radial-gradient(circle at center, #2e1114 0%, #000 100%); }");
        html.append(".hud { position: absolute; top: 20px; width: 100%; display: flex; justify-content: space-between; padding: 0 50px; box-sizing: border-box; z-index: 100; }");
        html.append(".health-box { background: rgba(0,0,0,0.8); padding: 15px; border-radius: 10px; border: 2px solid #0ff; width: 300px; box-shadow: 0 0 15px #0ff; }");
        html.append(".boss-box { border-color: #f00; box-shadow: 0 0 15px #f00; text-align: right; }");
        html.append(".bar { height: 20px; background: #333; margin-top: 10px; border-radius: 5px; overflow: hidden; position:relative;}");
        html.append(".fill { height: 100%; background: #0ff; width: 100%; transition: width 0.2s; }");
        html.append(".boss-fill { background: #f00; }");
        html.append(".entity { position: absolute; font-size: 150px; filter: drop-shadow(0 0 20px #0ff); white-space: nowrap; pointer-events:none;}");
        html.append("#boss { font-size: 250px; filter: drop-shadow(0 0 30px #f00); transform: scaleX(-1); }");
        html.append(".dmg-txt { position: absolute; color: yellow; font-size: 60px; font-weight: bold; pointer-events: none; animation: floatUp 1s ease-out forwards; text-shadow: 3px 3px 0 #000, -3px -3px 0 #000; z-index: 200; }");
        html.append(".miss-txt { position: absolute; color: white; font-size: 40px; font-weight: bold; pointer-events: none; animation: floatUp 1s ease-out forwards; text-shadow: 2px 2px 0 #000; z-index: 200; }");
        html.append("@keyframes floatUp { 0% { opacity: 1; transform: translateY(0); } 100% { opacity: 0; transform: translateY(-100px); } }");
        html.append(".attacking { transform: rotate(15deg) scale(1.1); filter: drop-shadow(0 0 40px #fff); }");
        html.append(".hurt { filter: brightness(2) drop-shadow(0 0 50px red) !important; animation: shake 0.2s; }");
        html.append("@keyframes shake { 0%,100%{transform:translateX(0)} 25%{transform:translateX(-10px)} 75%{transform:translateX(10px)} }");
        html.append(".controls { position: absolute; bottom: 20px; width: 100%; text-align: center; color: rgba(255,255,255,0.7); font-size: 22px; font-weight:bold; letter-spacing: 2px; }");
        html.append("#gameover { display: none; position: absolute; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.9); z-index: 300; justify-content: center; align-items: center; flex-direction: column; }");
        html.append(".btn-return { margin-top: 30px; padding: 20px 50px; font-size: 24px; background: #0ff; color: #000; text-decoration: none; font-weight: bold; border-radius: 10px; transition: 0.3s; box-shadow: 0 0 30px #0ff;}");
        html.append(".btn-return:hover { background: #fff; transform:scale(1.1); }");
        html.append("</style></head><body>");

        // UI DOM
        html.append("<div id='arena'>");
        html.append("<div class='hud'>");
        html.append("<div class='health-box'><h3 style='margin:0'>").append(current.getName()).append("</h3><div class='bar'><div id='heroFill' class='fill'></div></div><p id='heroHpTxt' style='margin:5px 0 0 0;font-weight:bold'>").append(heroMax).append(" / ").append(heroMax).append(" HP</p></div>");
        html.append("<div class='health-box boss-box'><h3 style='margin:0'>EL OBSERVADOR</h3><div class='bar'><div id='bossFill' class='fill boss-fill'></div></div><p id='bossHpTxt' style='margin:5px 0 0 0;font-weight:bold'>").append(bossMax).append(" / ").append(bossMax).append(" HP</p></div>");
        html.append("</div>");
        
        // ENTIDADES
        html.append("<div id='hero' class='entity'>").append(current.getAvatar()).append("</div>");
        html.append("<div id='boss' class='entity'>👾</div>");
        html.append("<div class='controls'>🏃[W A S D] MOVERSE | ⚔️[E] ATACAR</div>");
        html.append("</div>");

        // Game Over Screen
        html.append("<div id='gameover'><h1 id='goTitle' style='font-size:100px;margin:0;letter-spacing:5px;'></h1><a href='/' class='btn-return'>VOLVER A RPG EDGAR</a></div>");

        // SCRIPT INTERACTIVO: El Patrón Decorator alimenta las Variables en Tiempos de Ejecución
        html.append("<script>");
        html.append("const hero = document.getElementById('hero');");
        html.append("const boss = document.getElementById('boss');");
        html.append("const hFill = document.getElementById('heroFill');");
        html.append("const bFill = document.getElementById('bossFill');");
        html.append("const hTxt = document.getElementById('heroHpTxt');");
        html.append("const bTxt = document.getElementById('bossHpTxt');");
        
        // JAVA INYECTA STATS AQUI
        html.append("let hHp = ").append(heroMax).append(", hMax = ").append(heroMax).append(", hDmg = ").append(current.getDamage()).append(", hDef = ").append(current.getDefense()).append(", hSpd = ").append(Math.max(5, current.getSpeed()/1.5)).append(";");
        html.append("let bHp = ").append(bossMax).append(", bMax = ").append(bossMax).append(", bSpd = 3, bDmg = 55;");

        html.append("let hX = 200, hY = window.innerHeight/2;");
        html.append("let bX = window.innerWidth - 300, bY = window.innerHeight/2;");
        html.append("let keys = {}; let active = true;");
        html.append("let lastAtk = 0, lastBossAtk = 0;");
        
        html.append("window.addEventListener('keydown', (e) => keys[e.key.toLowerCase()] = true);");
        html.append("window.addEventListener('keyup', (e) => keys[e.key.toLowerCase()] = false);");

        html.append("function spawnTxt(x, y, txt, isDmg=true) {");
        html.append("  let d = document.createElement('div'); d.className=isDmg?'dmg-txt':'miss-txt'; d.innerText=txt;");
        html.append("  d.style.left = x+'px'; d.style.top = y+'px';");
        html.append("  document.getElementById('arena').appendChild(d);");
        html.append("  setTimeout(()=>d.remove(), 1000);");
        html.append("}");

        html.append("function end(won) {");
        html.append("  active = false; document.getElementById('gameover').style.display='flex';");
        html.append("  let t = document.getElementById('goTitle');");
        html.append("  if(won){ t.innerText='¡VICTORIA!'; t.style.color='#0f0'; t.style.textShadow='0 0 50px #0f0'; }");
        html.append("  else{ t.innerText='DERROTA'; t.style.color='#f00'; t.style.textShadow='0 0 50px #f00'; }");
        html.append("}");

        html.append("function collision(x1,y1,x2,y2,r) { let dx=x1-x2; let dy=y1-y2; return Math.sqrt(dx*dx+dy*dy)<r; }");

        html.append("function loop() {");
        html.append("  if(!active) return;");
        // Movimiento Héroe
        html.append("  if(keys['w'] && hY > 100) hY -= hSpd;");
        html.append("  if(keys['s'] && hY < window.innerHeight-150) hY += hSpd;");
        html.append("  if(keys['a'] && hX > 0) hX -= hSpd;");
        html.append("  if(keys['d'] && hX < window.innerWidth-150) hX += hSpd;");
        
        html.append("  if(hX > bX) hero.style.transform = 'scaleX(-1)'; else hero.style.transform = 'none';");

        // Ataque Héroe (E)
        html.append("  let now = Date.now();");
        html.append("  if(keys['e'] && now - lastAtk > 400) {"); // 400ms cooldown
        html.append("    lastAtk = now;");
        html.append("    hero.classList.add('attacking'); setTimeout(()=>hero.classList.remove('attacking'), 200);");
        html.append("    if(collision(hX, hY, bX, bY, 250)) {"); // Rango Hitbox
        html.append("      let dmg = hDmg + Math.floor(Math.random()*15); bHp = Math.max(0, bHp-dmg);");
        html.append("      spawnTxt(bX+100, bY, '-'+dmg); boss.classList.add('hurt'); setTimeout(()=>boss.classList.remove('hurt'),200);");
        html.append("      bFill.style.width = (bHp/bMax)*100+'%'; bTxt.innerText = bHp+' / '+bMax+' HP';");
        html.append("      if(bHp<=0) end(true);");
        html.append("    } else { spawnTxt(hX, hY-50, 'MISS', false); }");
        html.append("  }");

        // Boss IA
        html.append("  if(bX < hX) bX += bSpd; else if(bX > hX) bX -= bSpd;");
        html.append("  if(bY < hY) bY += bSpd; else if(bY > hY) bY -= bSpd;");
        html.append("  if(bX > hX) boss.style.transform = 'scaleX(-1)'; else boss.style.transform = 'none';");

        // Ataque Boss (Automático si está cerca, 1.5s cooldown)
        html.append("  if(collision(bX, bY, hX, hY, 200) && now - lastBossAtk > 1500) {");
        html.append("    lastBossAtk = now;");
        html.append("    boss.classList.add('attacking'); setTimeout(()=>boss.classList.remove('attacking'),200);");
        html.append("    let dmg = Math.max(5, bDmg - hDef + Math.floor(Math.random()*15)); hHp = Math.max(0, hHp-dmg);");
        html.append("    spawnTxt(hX+50, hY, '-'+dmg); hero.classList.add('hurt'); setTimeout(()=>hero.classList.remove('hurt'),200);");
        html.append("    hFill.style.width = (hHp/hMax)*100+'%'; hTxt.innerText = hHp+' / '+hMax+' HP';");
        html.append("    if(hHp<=0) end(false);");
        html.append("  }");

        // Actualizar visuales
        html.append("  hero.style.left = hX+'px'; hero.style.top = hY+'px';");
        html.append("  boss.style.left = bX+'px'; boss.style.top = bY+'px';");
        html.append("  requestAnimationFrame(loop);");
        html.append("}");
        
        html.append("loop();");
        html.append("</script>");
        
        html.append("</body></html>");
        return html.toString();
    }
}
