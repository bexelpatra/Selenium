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
<br>
![image](https://github.com/bexelpatra/Selenium/assets/83278536/8e0d7dde-b6b1-4c05-9ba8-bb161d89ce72)

login interface implication for "안전신문고"
<br>
![image](https://github.com/bexelpatra/Selenium/assets/83278536/15276b02-8e33-4c0e-97ed-507c8704b0f2)

web page loading interface
<br>
![image](https://github.com/bexelpatra/Selenium/assets/83278536/14c5ba9f-9f47-4e12-b101-416fdcfaf02f)

web page loading implication for "안전신문고"
<br>
![image](https://github.com/bexelpatra/Selenium/assets/83278536/af1f2537-e65e-41a4-9ed7-fc76326c32d7)

some funny points
1. web page loading time.
  - rather than 'WebDriverWait', i checked whether page is loaded or not by the <body> size.
   
2. image merging
  - to have full screen image, i captured screen image and scrolled down the page until the bottom. then merged it together.
  - at docker container, i have to adjust not only vertical, but also horizontal.
  - this content is in the ImageMerge.java

things to improve
1. mathing automatically chrome version and chrome driver
   - i decided to use library. [WebdriverManager](https://github.com/bonigarcia/webdrivermanager) was worked properly. but after chrome updated up to date version, this library throws error. so i commented out

review in velog
-[review about first edition](https://velog.io/@a45hvn/%EC%9E%90%EB%B0%94-%EC%85%80%EB%A0%88%EB%8B%88%EC%9B%80%EC%9C%BC%EB%A1%9C-%EC%9B%B9%EC%97%85%EB%AC%B4-%EC%9E%90%EB%8F%99%ED%99%94%ED%95%98%EA%B8%B0feat-%EA%B5%90%ED%86%B5%EC%95%88%EC%A0%84-%EA%B3%B5%EC%9D%B5%EC%A0%9C%EB%B3%B4%EB%8B%A8)
<br>
-[review about newest edition - not yet...](null)

