class UrlMappings {
    static mappings = {
      "/$controller/$action?/$id?"{
	      constraints {
			 // apply constraints here
		  }
	  }
	  "/fibonacci/$cnt" {
	      controller = 'demo'
	      action = 'fibo'
	  }
      "/"(view:"/index")
	  "500"(view:'/error')
	}
}
