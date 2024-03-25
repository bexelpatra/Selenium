Web screen crawling
this was made for the need of reporting "공익제보단"
at 2023 i crawled "스마트국민제보" web site. and the next year reporting site have shifted to "안전신문고"
as the site changed, i have to coding for new web site.

there are 2 key interface. one is "login" and the other is "web page loading".
1. login
   - using id and pw, get session.
2. web page loading
   - search web site url which you are going to screen capture.
3. open url
   - by js command 'window.open()' 
4. image capture and save and merge one file
   - on the web, full screen capture is happening by scroll down.
   - after save each screen capture, merge together.

login interface
![image](https://github.com/bexelpatra/Selenium/assets/83278536/8e0d7dde-b6b1-4c05-9ba8-bb161d89ce72)

login interface implication for "안전신문고"
![image](https://github.com/bexelpatra/Selenium/assets/83278536/15276b02-8e33-4c0e-97ed-507c8704b0f2)

web page loading interface
![image](https://github.com/bexelpatra/Selenium/assets/83278536/14c5ba9f-9f47-4e12-b101-416fdcfaf02f)

web page loading implication for "안전신문고"
![image](https://github.com/bexelpatra/Selenium/assets/83278536/af1f2537-e65e-41a4-9ed7-fc76326c32d7)

some funny points
1. web page loading time.
  - rather than 'WebDriverWait', i checked whether page is loaded or not by the <body> size.

2. image merging
  - to have full screen image, i captured screen image and scrolled down the page until the bottom. then merged it together.
  - this content is in the ImageMerge.java

