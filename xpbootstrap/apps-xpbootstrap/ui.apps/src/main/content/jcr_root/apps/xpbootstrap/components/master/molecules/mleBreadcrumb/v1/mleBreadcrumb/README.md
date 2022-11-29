
Breadcrumb Molecule (v1)
====
The Breadcrumb Component displays the position of the current page within the site hierarchy, allowing page visitors to navigate the page hierarchy from their current location. This is often integrated into page headers or footers.

## Features

1. Start level
2. Option to show hidden navigation items
3. Exclude the current page from the breadcrumb

### Use Object
The Banner molecule uses the `com.xperience.aem.xpbootstrap.core.models.molecules.MleBreadcrumb` Sling model as its Use-object.

### Component Policy Configuration Properties

The following configuration properties are used:

1. `./startLevel` - the level at which to start the breadcrumb: 0 = /content, 1 = /content/site, etc.
2. `./showHidden` - if `true`, show navigation items hidden via a ./hideInNav property in the breadcrumb.
3. `./hideCurrent` - if `true`, don't display the current page in the breadcrumb.

### Edit Dialog Properties
The following properties are written to JCR for this Accordion component and are expected to be available as `Resource` properties:

1. `./startLevel` - the level at which to start the breadcrumb: 0 = /content, 1 = /content/site, etc.
2. `./showHidden` - if `true`, show navigation items hidden via a ./hideInNav property in the breadcrumb.
3. `./hideCurrent` - if `true`, don't display the current page in the breadcrumb.

## Client Libraries
It uses bootstraps breadcrumb component CSS [https://getbootstrap.com/docs/5.0/components/breadcrumb/]
It should be added to a relevant site client library using the `embed` property.


## Information
* **Vendor**: Deloitte
* **Version**: v1
* **Compatibility**: AEM 6.5
* **Status**: production-ready
* **Documentation**: [http://localhost:4502/content/adlc-component-library/us/en/home/breadcrumb-molecule.html](http://localhost:4502/content/adlc-component-library/us/en/home/breadcrumb-molecule.html)
* **Component Library**: [http://localhost:4502/content/adlc-component-library/us/en/home/breadcrumb-molecule.html](http://localhost:4502/content/adlc-component-library/us/en/home/breadcrumb-molecule.html)
* **Author**: [Deloitte]()
* **Co-authors**: [Deloitte](), [BhushanBashire](https://github.com/BhushanBashire)

