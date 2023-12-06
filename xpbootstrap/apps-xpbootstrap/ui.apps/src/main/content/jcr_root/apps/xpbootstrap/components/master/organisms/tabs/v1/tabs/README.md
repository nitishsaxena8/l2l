
Tabs Organism (v1)
====
Tabs organism written in HTL. Tabs is a container component

## Features

* Allows addition of tab panel components of varying resource type.
* Allowed components can be configured through policy configuration.
* Navigation to tab panels via tabs.
* Editing features for tab panels (adding, removing, editing, re-ordering).
* Allows deep linking into a specific tab by passing the tab id as the URL fragment

### Use Object
The Banner molecule uses the `com.xperience.aem.xpbootstrap.core.models.organisms.Tabs` Sling model as its Use-object.

### Component Policy Configuration Properties
The component policy dialog allows definition of allowed components for the Tabs.


### Edit Dialog Properties
The following properties are written to JCR for this Accordion component and are expected to be available as `Resource` properties:

1. `./activeItem` - defines the name of the item that is active by default.
2. `./id` - defines the component HTML ID attribute.
3. `./accessibilityLabel` - defines an accessibility label for the tabs.

The edit dialog also allows editing of Accordion items (adding, removing, naming, re-ordering).

## Client Libraries
It uses bootstraps breadcrumb component CSS [https://getbootstrap.com/docs/5.0/components/navs-tabs/#tabs]
It should be added to a relevant site client library using the `embed` property.


## Information
* **Vendor**: Deloitte
* **Version**: v1
* **Compatibility**: AEM 6.5
* **Status**: production-ready
* **Documentation**: [http://localhost:4502/content/adlc-component-library/us/en/home/tabs-organism.html](http://localhost:4502/content/adlc-component-library/us/en/home/tabs-organism.html)
* **Component Library**: [http://localhost:4502/content/adlc-component-library/us/en/home/tabs-organism.html](http://localhost:4502/content/adlc-component-library/us/en/home/tabs-organism.html)
* **Author**: [Deloitte]()
* **Co-authors**: [Deloitte](), [BhushanBashire](https://github.com/BhushanBashire)

