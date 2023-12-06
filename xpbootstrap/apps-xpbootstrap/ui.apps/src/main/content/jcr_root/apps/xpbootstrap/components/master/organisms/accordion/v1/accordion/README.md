
Accordion Organism (v1)
====
Accordion organism written in HTL. Accordion is a container component

## Features

* Allows addition of accordion items of varying resource type.
* Allowed components can be configured through policy configuration.
* Toggle accordion panels from accordion header controls.
* Ability to force a single panel to be displayed.
* Items expanded by default are configurable.
* Editing features for accordion items (adding, removing, editing, re-ordering).
* Allows deep linking into a specific panel by passing the panel id as the URL fragment

### Use Object
The Accordion molecule uses the `com.xperience.aem.xpbootstrap.core.models.organisms.Accordion` Sling model as its Use-object.

### Component Policy Configuration Properties
The following configuration properties are used:

1. `./allowedHeadingElements` - the heading elements (`h1` - `h6`) that are allowed to be selected in the edit dialog.
2. `./headingElement` - the default heading element (`h1` - `h6`) to use for the accordion headers.


### Edit Dialog Properties
The following properties are written to JCR for this Accordion component and are expected to be available as `Resource` properties:

1. `./singleExpansion` - `true` if only one panel should be allowed to be expanded at a time, `false` otherwise.
2. `./expandedItems` - defines the names of the items that are expanded by default.
3. `./headingElement` - defines the heading element to use for the accordion headers (`h2` - `h6`).
4. `./id` - defines the component HTML ID attribute.

The edit dialog also allows editing of Accordion items (adding, removing, naming, re-ordering).

## Client Libraries
It uses bootstraps breadcrumb component CSS [https://getbootstrap.com/docs/5.0/components/accordion/]
It should be added to a relevant site client library using the `embed` property.


## Information
* **Vendor**: Deloitte
* **Version**: v1
* **Compatibility**: AEM 6.5
* **Status**: production-ready
* **Documentation**: [http://localhost:4502/content/adlc-component-library/us/en/home/accordion-organism.html](http://localhost:4502/content/adlc-component-library/us/en/home/accordion-organism.html)
* **Component Library**: [http://localhost:4502/content/adlc-component-library/us/en/home/accordion-organism.html](http://localhost:4502/content/adlc-component-library/us/en/home/accordion-organism.html)
* **Author**: [Deloitte]()
* **Co-authors**: [Deloitte](), [BhushanBashire](https://github.com/BhushanBashire)

