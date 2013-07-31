var profile = (function () {
    return {

        // relative to this file
        basePath:"./src/main/js",

        // relative to base path
        releaseDir:"../../../target/dashboard",

        releaseName:"js",

        // Builds a new release.
        action:"release",

        // Uses Closure Compiler as the JavaScript minifier. This can also be set to "shrinksafe" to use ShrinkSafe,
        // though ShrinkSafe is deprecated and not recommended.
        optimize:"closure",

        // We're building layers, so we need to set the minifier to use for those, too.
        layerOptimize:"closure",

        // Strips all comments and whitespace from CSS files and inlines @imports where possible.
        cssOptimize:"comments",

        // Excludes tests, demos, and original template files from being included in the built version.
        mini:true,

        // Strips all calls to console functions within the code. You can also set this to "warn" to strip everything
        // but console.error, and any other truthy value to strip everything but console.warn and console.error.
        // This defaults to "normal" (strip all but warn and error) if not provided.
        // Setting it to none to help debugging
        stripConsole:"none",

        // The default selector engine is not included by default in a dojo.js build in order to make mobile builds
        // smaller. We add it back here to avoid that extra HTTP request. There is also a "lite" selector available; if
        // you use that, you will need to set the `selectorEngine` property in `app/run.js`, too. (The "lite" engine is
        // only suitable if you are not supporting IE7 and earlier.)
        selectorEngine:"acme",

        // since this build it intended to be utilized with properly-expressed AMD modules;
        // don't insert absolute module ids into the modules
        insertAbsMids:0,

        // Providing hints to the build system allows code to be conditionally removed on a more granular level than
        // simple module dependencies can allow. This is especially useful for creating tiny mobile builds.
        // Keep in mind that dead code removal only happens in minifiers that support it! Currently, only Closure Compiler
        // to the Dojo build system with dead code removal.
        // A documented list of has-flags in use within the toolkit can be found at
        // <http://dojotoolkit.org/reference-guide/dojo/has.html>.
        staticHasFeatures:{
            // The trace & log APIs are used for debugging the loader, so we do not need them in the build.
            'dojo-trace-api':0,
            'dojo-log-api':0,

            // This causes normally private loader data to be exposed for debugging. In a release build, we do not need
            // that either.
            'dojo-publish-privates':0,

            // This application is pure AMD, so get rid of the legacy loader.
            'dojo-sync-loader':0,

            // `dojo-xhr-factory` relies on `dojo-sync-loader`, which we have removed.
            'dojo-xhr-factory':0,

            // We are not loading tests in production, so we can get rid of some test sniffing code.
            'dojo-test-sniff':0,

            "config-deferredInstrumentation":0,
            "config-dojo-loader-catches":0,
            "config-tlmSiblingOfDojo":0,
            "dojo-amd-factory-scan":0,
            "dojo-combo-api":0,
            "dojo-config-api":1,
            "dojo-config-require":0,
            "dojo-debug-messages":0,
            "dojo-dom-ready-api":1,
            "dojo-firebug":0,
            "dojo-guarantee-console":1,
            "dojo-has-api":1,
            "dojo-inject-api":1,
            "dojo-loader":1,
            "dojo-modulePaths":0,
            "dojo-moduleUrl":0,
            "dojo-requirejs-api":0,
            "dojo-sniff":0,
            "dojo-timeout-api":0,
            "dojo-undef-api":0,
            "dojo-v1x-i18n-Api":1,
            "dom":1,
            "host-browser":1,
            "extend-dojo":1
        },

        packages:[
            {
                name:"dojo",
                location:"dojo"
            },
            {
                name:"dijit",
                location:"dijit"
            },
            {
                name:"dojox",
                location:"dojox"
            },
            {
                name:"gridx",
                location:"gridx"
            },
            {
                name:"dashboard",
                location:"dashboard"
            }
        ],

        depsDumpFilename:"dashboard.json",
        depsDumpDotFilename:"dashboard.dot",
        dotModules:""

    };
})();

Packages.com.google.javascript.jscomp.Compiler.setLoggingLevel(Packages.java.util.logging.Level.SEVERE);
