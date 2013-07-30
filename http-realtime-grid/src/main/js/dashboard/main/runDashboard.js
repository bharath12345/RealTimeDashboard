/**
 * This file is used to reconfigure parts of the loader at runtime for this application. We've put this extra
 * configuration in a separate file, instead of adding it directly to the JSP
 *
 */
require({
    // The base path for all packages and modules.
    baseUrl:'./js/dashboard',

    // A list of packages to register.
    // You can't require "dashboard" and get dashboard/Dashboard.js if you do not register the "dashboard" package
    // register all packages all the time to make life easier

    packages:[
        // If you are registering a package that has an identical name and location, you can just pass a string
        // instead, and it will configure it using that string for both the "name" and "location" properties. Handy!
        // ALSO look into the definition of "main" in package.json
        {name:'dojo', location:'../dojo'},
        {name:'dijit', location:'../dijit'},
        {name:'dojox', location:'../dojox'},
        {name:'gridx', location:'../gridx'},
        {name:'Dashboard', location:'.', main:'Dashboard' }
    ]
// Require `Dashboard`. This loads the main application module, `dashboard/Dashboard`, since we registered the `Dashboard` package above.
}, [ 'Dashboard' ]);
