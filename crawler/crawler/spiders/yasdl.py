from scrapy import Spider
from scrapy import Request


class MyDict(dict):

    def __repr__(self):
        return self['title']


class YasdlSpider(Spider):
    name = 'yasdl'

    start_urls = ['https://www.yasdl.com/']

    custom_settings = {
        'CONCURRENT_REQUESTS': 64,
        'DOWNLOAD_DELAY': 0
    }

    def parse(self, response):
        last_url_link = response.xpath('//div[contains(@class, "pagination")]/div/a[contains(@title, "آخر")]/@href').get()
        last_page_number = int(last_url_link.split('/')[-1])
        for i in range(1, last_page_number):
            yield Request(
                url=f'https://www.yasdl.com/page/{i}',
                callback=self.parse_pages
            )

    def parse_pages(self, response):
        articles = response.xpath('//article')
        links = articles.xpath('h2/a/@href').getall()
        titles = articles.xpath('h2/a/@title').getall()
        body_html = articles.xpath('//article/div[contains(@class, "post-body")]/div[contains(@class, "post-text")]')

        for item in zip(titles, links, body_html):
            body_list = item[2].xpath('p[contains(@style, "justify")]/text()').getall()
            body = ''
            for t in body_list:
                body += (t + '\n')

            yield MyDict({
                'title': item[0],
                'link': item[1],
                'body': body,
                'siteName': 'yasdl'
            })

