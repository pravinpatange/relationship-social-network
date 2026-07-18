const puppeteer = require('puppeteer');

(async () => {
  const browser = await puppeteer.launch({ headless: 'new' });
  const page = await browser.newPage();
  
  const errors = [];
  page.on('console', msg => {
    if (msg.type() === 'error') errors.push(msg.text());
  });
  page.on('pageerror', err => errors.push(err.message));

  console.log("Navigating to feed...");
  await page.goto('http://localhost:4200/app/feed', { waitUntil: 'networkidle2', timeout: 10000 }).catch(e => console.log(e));
  
  await new Promise(r => setTimeout(r, 2000));
  
  const html = await page.content();
  console.log("Found app-feed?", html.includes('<app-feed'));
  console.log("Found loading spinner?", html.includes('animate-spin'));
  console.log("Found 'No posts yet'?", html.includes('No posts yet'));
  
  console.log("Console Errors:", errors);
  
  await browser.close();
})();
